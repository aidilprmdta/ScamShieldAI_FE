package com.example.scamshieldai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scamshieldai.analysis.AnalysisCoordinator
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.model.EducationCatalog
import com.example.scamshieldai.model.EducationMatcher
import com.example.scamshieldai.settings.AppPreferences
import com.example.scamshieldai.ui.components.AnalyzingOverlay
import com.example.scamshieldai.ui.screens.*
import com.example.scamshieldai.ui.state.ScamShieldAppState
import kotlinx.coroutines.launch

@Composable
fun ScamShieldNavGraph(
    navController: NavHostController,
    appState: ScamShieldAppState,
    analysisCoordinator: AnalysisCoordinator,
    modifier: Modifier = Modifier
) {
    val educationContents = remember { EducationCatalog.all }

    NavHost(
        navController = navController,
        startDestination = Screen.Login,
        modifier = modifier
    ) {
        // ===== AUTH =====
        composable(Screen.Login) {
            val loginContext = LocalContext.current
            LoginScreen(
                onLoginSuccess = {
                    appState.scope.launch {
                        AppPreferences.setSavedToken(
                            loginContext,
                            AuthTokenStore.idToken,
                            AuthTokenStore.refreshToken
                        )
                    }
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                }
            )
        }

        composable(Screen.Register) {
            RegisterScreen(
                onRegisterSuccess = {
                    appState.showSuccess("Registrasi berhasil! Silakan masuk.")
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ===== MAIN TABS =====
        composable(Screen.Home) {
            HomeScreen(
                userName = appState.userDisplayName,
                threatCount = appState.threatCount,
                onScanModeSelected = { mode ->
                    when (mode) {
                        "chat" -> navController.navigate(Screen.ScanChat)
                        "link" -> navController.navigate(Screen.CheckLink)
                        "screenshot" -> navController.navigate(Screen.ScanScreenshot)
                        "qr" -> navController.navigate(Screen.ScanQR)
                        else -> navController.navigate(Screen.Analyzing)
                    }
                },
                onEducationSelected = {
                    navController.navigate(Screen.EducationCenter) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onHistoryClick = {
                    navController.navigate(Screen.History)
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notifications)
                }
            )
        }

        composable(Screen.History) {
            LaunchedEffect(Unit) { appState.fetchHistory() }
            HistoryScreen(
                historyList = appState.historyList,
                isLoading = appState.isHistoryLoading,
                errorMessage = appState.historyError,
                isLoggedIn = appState.isLoggedIn,
                onBack = { navController.popBackStack() },
                onItemClick = { item ->
                    appState.lastResult = item.result
                    appState.resultSessionId += 1
                    navController.navigate(Screen.Result) {
                        launchSingleTop = true
                    }
                },
                onDeleteItem = { item ->
                    appState.deleteHistoryItem(item)
                },
                onRefresh = { appState.fetchHistory() }
            )
        }

        composable(Screen.Profile) {
            LaunchedEffect(Unit) { appState.fetchBadgeCounts() }
            ProfileScreen(
                userName = appState.userDisplayName.ifEmpty {
                    appState.userEmail.substringBefore("@").ifEmpty { "Pengguna" }
                },
                userEmail = appState.userEmail,
                scanCount = appState.historyList.size,
                threatCount = appState.threatCount,
                isAdmin = appState.isAdmin,
                pendingMyReportsCount = appState.pendingMyReportsCount,
                pendingAdminReportsCount = appState.pendingAdminReportsCount,
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile)
                },
                onAdminReportsClick = {
                    appState.fetchAdminReports()
                    navController.navigate(Screen.AdminReports)
                },
                onMyReportsClick = {
                    appState.fetchMyReports()
                    navController.navigate(Screen.MyReports)
                },
                onSecurityClick = {
                    navController.navigate(Screen.SecurityPrivacy)
                },
                onAboutClick = {
                    navController.navigate(Screen.About)
                },
                onHelpClick = {
                    navController.navigate(Screen.HelpCenter)
                },
                onLogout = {
                    appState.logout()
                    navController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ===== SCAN & ANALYZE =====
        composable(Screen.ScanChat) {
            ScanChatScreen(
                onBack = { navController.popBackStack() },
                onAnalyze = { text ->
                    analysisCoordinator.start(
                        errorPrefix = "Gagal menganalisis",
                        remindLoginOnSuccess = true
                    ) { appState.repository.analyzeChat(text) }
                }
            )
        }

        composable(Screen.CheckLink) {
            CheckLinkScreen(
                onBack = { navController.popBackStack() },
                onCheck = { url ->
                    analysisCoordinator.start(
                        errorPrefix = "Gagal menganalisis link",
                        remindLoginOnSuccess = true
                    ) { appState.repository.analyzeLink(url) }
                }
            )
        }

        composable(Screen.ScanScreenshot) {
            ScanScreenshotScreen(
                onBack = { navController.popBackStack() },
                onAnalyzeText = { text ->
                    analysisCoordinator.start(errorPrefix = "Gagal menganalisis screenshot") {
                        appState.repository.analyzeChat(text, source = "screenshot_ocr")
                    }
                }
            )
        }

        composable(Screen.ScanQR) {
            ScanQRScreen(
                onBack = { navController.popBackStack() },
                onScanned = { data ->
                    analysisCoordinator.start(errorPrefix = "Gagal menganalisis QR") {
                        appState.repository.analyzeQr(data)
                    }
                }
            )
        }

        composable(Screen.Analyzing) {
            AnalyzingOverlay()
        }

        composable(Screen.Result) {
            val result = appState.lastResult ?: ScanResult(
                type = "unknown",
                riskScore = 0,
                riskLevel = RiskLevel.LOW,
                inputSummary = "",
                flags = emptyList(),
                explanation = "Tidak ada hasil analisis.",
                recommendation = "Silakan lakukan scan terlebih dahulu."
            )
            val relatedEducation = remember(result.relatedArticle, result.riskLevel, appState.resultSessionId) {
                val shouldOffer = result.riskLevel != RiskLevel.LOW || !result.relatedArticle.isNullOrBlank()
                if (!shouldOffer) null
                else EducationMatcher.findByCategory(
                    category = result.relatedArticle,
                    contents = educationContents,
                    fallbackToFeatured = result.riskLevel != RiskLevel.LOW
                )
            }
            key(appState.resultSessionId, result.inputSummary, result.riskScore) {
                ResultScreen(
                    result = result,
                    onBackToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Result) { inclusive = true }
                        }
                    },
                    onHistoryClick = {
                        navController.navigate(Screen.History)
                    },
                    onBlockDeleteClick = {
                        navController.navigate(Screen.BlockDelete)
                    },
                    onReportClick = {
                        navController.navigate(Screen.Report)
                    },
                    learnMoreTitle = relatedEducation?.title,
                    onLearnMoreClick = relatedEducation?.let { content ->
                        {
                            navController.navigate(Screen.educationDetailRoute(content.id))
                        }
                    }
                )
            }
        }

        // ===== EDUCATION =====
        composable(Screen.EducationCenter) {
            EducationCenterScreen(
                onBack = { navController.popBackStack() },
                onItemClick = { item ->
                    if (item.type == "KUIS") {
                        navController.navigate(Screen.Quiz)
                    } else {
                        navController.navigate(Screen.educationDetailRoute(item.id))
                    }
                }
            )
        }

        composable(
            route = Screen.EducationDetail,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val content = EducationCatalog.findById(id)
            EducationDetailScreen(
                content = content,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Quiz) {
            QuizScreen(
                onBack = { navController.popBackStack() },
                onFinish = { navController.popBackStack() }
            )
        }

        // ===== PROFILE & SETTINGS =====
        composable(Screen.EditProfile) {
            EditProfileScreen(
                initialName = appState.userDisplayName.ifEmpty {
                    appState.userEmail.substringBefore("@")
                },
                initialEmail = appState.userEmail,
                onBack = { navController.popBackStack() },
                onSave = { name, email, onDone ->
                    appState.updateProfile(name, email, onDone)
                }
            )
        }

        composable(Screen.SecurityPrivacy) {
            SecurityPrivacyScreen(
                onBack = { navController.popBackStack() },
                onManagePermissions = { navController.navigate(Screen.PermissionManagement) },
                onChangePassword = {
                    if (!appState.isLoggedIn) {
                        appState.showError("Login diperlukan untuk mengubah kata sandi")
                    } else {
                        navController.navigate(Screen.ChangePassword)
                    }
                },
                onAutoCleanEnabled = {
                    appState.autoCleanHistory()
                }
            )
        }

        composable(Screen.ChangePassword) {
            ChangePasswordScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { currentPassword, newPassword, onDone ->
                    appState.changePassword(currentPassword, newPassword) { res ->
                        onDone(res)
                        if (res.isSuccess) {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(Screen.PermissionManagement) {
            PermissionManagementScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Notifications) {
            LaunchedEffect(Unit) { appState.fetchNotifications() }
            NotificationScreen(
                notifications = appState.notificationList,
                isLoading = appState.isNotificationsLoading,
                errorMessage = appState.notificationsError,
                onBack = { navController.popBackStack() },
                onRefresh = { appState.fetchNotifications() },
                onNotificationClick = { item ->
                    appState.markNotificationRead(item.id)
                    val reportId = item.data?.get("report_id")?.toString()
                    if (!reportId.isNullOrBlank() &&
                        (item.type == "report_status_updated" || item.type == "new_report")
                    ) {
                        if (item.type == "new_report" && appState.isAdmin) {
                            navController.navigate(Screen.AdminReports)
                        } else {
                            navController.navigate(Screen.reportStatusRoute(reportId))
                        }
                    }
                }
            )
        }

        composable(Screen.About) {
            AboutScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.HelpCenter) {
            HelpCenterScreen(onBack = { navController.popBackStack() })
        }

        // ===== REPORTS & ACTIONS =====
        composable(Screen.BlockDelete) {
            BlockDeleteScreen(
                onBackToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                    appState.showSuccess("Tindakan berhasil dilakukan")
                }
            )
        }

        composable(Screen.Report) {
            ReportScreen(
                onBack = { navController.popBackStack() },
                onSubmit = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                    appState.showSuccess("Laporan berhasil dikirim")
                },
                onSubmitReport = { type, content, note, onResult ->
                    appState.submitReport(type, content, note, onResult)
                }
            )
        }

        composable(Screen.MyReports) {
            MyReportsScreen(
                reports = appState.myReports,
                isLoading = appState.isMyReportsLoading,
                onBack = { navController.popBackStack() },
                onReportClick = { report ->
                    navController.navigate(Screen.reportStatusRoute(report.reportId))
                },
                onRefresh = { appState.fetchMyReports() }
            )
        }

        composable(
            route = Screen.ReportStatus,
            arguments = listOf(navArgument("report_id") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("report_id").orEmpty()
            LaunchedEffect(reportId) {
                appState.fetchReportDetail(reportId)
            }
            ReportStatusScreen(
                report = if (appState.reportDetail?.reportId == reportId) appState.reportDetail else null,
                isLoading = appState.isReportDetailLoading,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminReports) {
            if (!appState.isAdmin) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Profile) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                    }
                    appState.showError("Akses ditolak: hanya admin yang bisa mengelola laporan.")
                }
            } else {
                AdminReportsScreen(
                    reports = appState.adminReports,
                    isLoading = appState.isAdminLoading,
                    onBack = { navController.popBackStack() },
                    onRefresh = { appState.fetchAdminReports() },
                    onVerify = { reportId -> appState.verifyAdminReport(reportId) },
                    onReject = { reportId -> appState.rejectAdminReport(reportId) }
                )
            }
        }
    }
}
