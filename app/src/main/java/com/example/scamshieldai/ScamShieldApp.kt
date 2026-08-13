package com.example.scamshieldai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scamshieldai.analysis.AnalysisCoordinator
import com.example.scamshieldai.analysis.mapApiResultToScanResult
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.model.EducationCatalog
import com.example.scamshieldai.model.EducationMatcher
import com.example.scamshieldai.network.ScamShieldRepository
import com.example.scamshieldai.settings.AppPreferences
import com.example.scamshieldai.settings.HistoryAutoCleaner
import com.example.scamshieldai.ui.components.AnalyzingOverlay
import com.example.scamshieldai.ui.components.FloatingNavBar
import com.example.scamshieldai.ui.components.NavigationItemData
import com.example.scamshieldai.ui.feedback.AppSnackbarHost
import com.example.scamshieldai.ui.feedback.showError
import com.example.scamshieldai.ui.feedback.showInfo
import com.example.scamshieldai.ui.feedback.showSuccess
import com.example.scamshieldai.ui.screens.AboutScreen
import com.example.scamshieldai.ui.screens.AdminReportsScreen
import com.example.scamshieldai.ui.screens.BlockDeleteScreen
import com.example.scamshieldai.ui.screens.ChangePasswordScreen
import com.example.scamshieldai.ui.screens.CheckLinkScreen
import com.example.scamshieldai.ui.screens.EditProfileScreen
import com.example.scamshieldai.ui.screens.EducationCenterScreen
import com.example.scamshieldai.ui.screens.EducationDetailScreen
import com.example.scamshieldai.ui.screens.HelpCenterScreen
import com.example.scamshieldai.ui.screens.HistoryItem
import com.example.scamshieldai.ui.screens.HistoryScreen
import com.example.scamshieldai.ui.screens.HomeScreen
import com.example.scamshieldai.ui.screens.LoginScreen
import com.example.scamshieldai.ui.screens.MyReportsScreen
import com.example.scamshieldai.ui.screens.NotificationScreen
import com.example.scamshieldai.ui.screens.PermissionManagementScreen
import com.example.scamshieldai.ui.screens.ProfileScreen
import com.example.scamshieldai.ui.screens.QuizScreen
import com.example.scamshieldai.ui.screens.RegisterScreen
import com.example.scamshieldai.ui.screens.ReportScreen
import com.example.scamshieldai.ui.screens.ReportStatusScreen
import com.example.scamshieldai.ui.screens.ResultScreen
import com.example.scamshieldai.ui.screens.RiskLevel
import com.example.scamshieldai.ui.screens.ScanChatScreen
import com.example.scamshieldai.ui.screens.ScanQRScreen
import com.example.scamshieldai.ui.screens.ScanResult
import com.example.scamshieldai.ui.screens.ScanScreenshotScreen
import com.example.scamshieldai.ui.screens.SecurityPrivacyScreen
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ScamShieldApp(
    pendingNotificationRoute: String? = null,
    onNotificationRouteConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()
    val repository = remember { ScamShieldRepository() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val token = AuthTokenStore.idToken
    
    val snackbarHostState = remember { SnackbarHostState() }
    var lastResult by remember { mutableStateOf<ScanResult?>(null) }
    var resultSessionId by remember { mutableIntStateOf(0) }
    var completedEducationIds by remember { mutableStateOf(setOf<String>()) }
    val historyList = remember { mutableStateListOf<HistoryItem>() }
    var isHistoryLoading by remember { mutableStateOf(false) }
    var historyError by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf("") }
    var userDisplayName by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    val adminReports = remember { mutableStateListOf<com.example.scamshieldai.network.AdminReportItem>() }
    var isAdminLoading by remember { mutableStateOf(false) }
    val myReports = remember { mutableStateListOf<com.example.scamshieldai.network.UserReportItem>() }
    var isMyReportsLoading by remember { mutableStateOf(false) }
    var reportDetail by remember { mutableStateOf<com.example.scamshieldai.network.UserReportItem?>(null) }
    var isReportDetailLoading by remember { mutableStateOf(false) }
    val notificationList = remember { mutableStateListOf<com.example.scamshieldai.network.NotificationItem>() }
    var isNotificationsLoading by remember { mutableStateOf(false) }
    var notificationsError by remember { mutableStateOf<String?>(null) }
    var pendingMyReportsCount by remember { mutableIntStateOf(0) }
    var pendingAdminReportsCount by remember { mutableIntStateOf(0) }

    fun showError(message: String) {
        coroutineScope.launch { snackbarHostState.showError(message) }
    }

    fun showSuccess(message: String) {
        coroutineScope.launch { snackbarHostState.showSuccess(message) }
    }

    fun showInfo(message: String) {
        coroutineScope.launch { snackbarHostState.showInfo(message) }
    }

    LaunchedEffect(Unit) {
        val savedToken = AppPreferences.savedTokenFlow(context).first()
        val savedRefresh = AppPreferences.savedRefreshTokenFlow(context).first()
        if (savedToken != null) {
            AuthTokenStore.setToken(savedToken, savedRefresh)
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    lateinit var fetchHistory: () -> Unit
    val analysis = remember {
        AnalysisCoordinator(
            scope = coroutineScope,
            navController = navController,
            onClearPendingResult = { lastResult = null },
            onResultReady = { result ->
                lastResult = result
                resultSessionId += 1
            },
            onFetchHistory = { fetchHistory() },
            showError = { showError(it) },
            showInfo = { showInfo(it) },
            isLoggedIn = { AuthTokenStore.idToken != null }
        )
    }

    fetchHistory = {
        coroutineScope.launch {
            isHistoryLoading = true
            historyError = null

            val autoClean = AppPreferences.autoCleanEnabledFlow(context).first()
            if (autoClean && token != null) {
                HistoryAutoCleaner.cleanOldHistory(repository)
            }

            repository.getHistory().onSuccess { response ->
                historyList.clear()
                for (item in response.data) {
                    val scanResult = mapApiResultToScanResult(item)
                    historyList.add(HistoryItem(item.scanId ?: "", scanResult, "Tersimpan"))
                }
            }.onFailure {
                historyError = it.message ?: "Gagal memuat riwayat"
                showError("Gagal memuat riwayat: ${it.message}")
            }
            isHistoryLoading = false
        }
    }

    val fetchReportBadgeCounts: () -> Unit = {
        coroutineScope.launch {
            if (token == null) {
                pendingMyReportsCount = 0
                pendingAdminReportsCount = 0
                return@launch
            }
            repository.getMyPendingReportCount().onSuccess { pendingMyReportsCount = it }
            if (isAdmin) {
                repository.getAdminPendingReportCount().onSuccess { pendingAdminReportsCount = it }
            } else {
                pendingAdminReportsCount = 0
            }
        }
    }

    val fetchMyReports: () -> Unit = {
        coroutineScope.launch {
            isMyReportsLoading = true
            repository.getMyReports().onSuccess { list ->
                myReports.clear()
                myReports.addAll(list)
            }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
            isMyReportsLoading = false
        }
    }

    val fetchNotifications: () -> Unit = {
        coroutineScope.launch {
            if (AuthTokenStore.idToken == null) {
                notificationList.clear()
                notificationsError = "Login untuk melihat notifikasi"
                return@launch
            }
            isNotificationsLoading = true
            notificationsError = null
            repository.getNotifications()
                .onSuccess {
                    notificationList.clear()
                    notificationList.addAll(it)
                }
                .onFailure {
                    notificationsError = it.message ?: "Gagal memuat notifikasi"
                }
            isNotificationsLoading = false
        }
    }

    LaunchedEffect(token) {
        fetchHistory()
        if (token != null) {
            repository.getMe().onSuccess { me ->
                userEmail = me.email ?: ""
                userDisplayName = me.displayName?.takeIf { it.isNotBlank() }
                    ?: me.email?.substringBefore("@").orEmpty()
                isAdmin = me.admin
                coroutineScope.launch {
                    repository.getMyPendingReportCount().onSuccess { pendingMyReportsCount = it }
                    if (me.admin) {
                        repository.getAdminPendingReportCount().onSuccess { pendingAdminReportsCount = it }
                    } else {
                        pendingAdminReportsCount = 0
                    }
                }
            }
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { fcmToken: String ->
                    coroutineScope.launch { repository.registerFcmToken(fcmToken) }
                }
            fetchNotifications()
        } else {
            userEmail = ""
            userDisplayName = ""
            isAdmin = false
            pendingMyReportsCount = 0
            pendingAdminReportsCount = 0
            notificationList.clear()
        }
    }

    LaunchedEffect(pendingNotificationRoute, token, isAdmin) {
        val route = pendingNotificationRoute ?: return@LaunchedEffect
        when {
            route.startsWith("report_status/") -> {
                if (token == null) {
                    showError("Silakan login untuk melihat laporan.")
                } else {
                    val reportId = route.removePrefix("report_status/")
                    navController.navigate("report_status/$reportId")
                }
            }
            route == "admin_reports" -> {
                if (!isAdmin) {
                    showError("Akses ditolak: hanya admin.")
                } else {
                    coroutineScope.launch {
                        isAdminLoading = true
                        repository.getAdminReports().onSuccess { list ->
                            adminReports.clear()
                            adminReports.addAll(list)
                        }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
                        isAdminLoading = false
                    }
                    navController.navigate("admin_reports")
                }
            }
        }
        onNotificationRouteConsumed()
    }

    val educationContents = EducationCatalog.all

    val profileNavBadge = if (isAdmin) pendingAdminReportsCount else pendingMyReportsCount

    val navItems = remember(profileNavBadge) {
        listOf(
            NavigationItemData("Beranda", "home", Icons.Filled.Home, Icons.Outlined.Home),
            NavigationItemData("Riwayat", "history", Icons.Filled.History, Icons.Outlined.History),
            NavigationItemData("Edukasi", "education_center", Icons.Filled.School, Icons.Outlined.School),
            NavigationItemData("Profil", "profile", Icons.Filled.Person, Icons.Outlined.Person, badgeCount = profileNavBadge)
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: "home"

    // Logic for highlighting the correct tab even in sub-pages
    val selectedRoute = when {
        currentRoute.startsWith("education_detail") -> "education_center"
        else -> currentRoute
    }

    // Logic for showing Navbar: Hide on scanning, result, and auth screens
    val hideNavBarRoutes = listOf(
        "login", "register",
        "scan_chat", "check_link", "scan_screenshot", "scan_qr", 
        "analyzing", "result", "block_delete", "report", "quiz_screen",
        "education_detail", "security_privacy", "change_password", "permission_management", "notifications", "about",
        "admin_reports", "my_reports", "report_status", "edit_profile", "help_center"
    )
    val showNavBar = !hideNavBarRoutes.any { currentRoute.startsWith(it) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("login") {
                    val loginContext = LocalContext.current
                    LoginScreen(
                        onLoginSuccess = {
                            coroutineScope.launch {
                                AppPreferences.setSavedToken(loginContext, AuthTokenStore.idToken, AuthTokenStore.refreshToken)
                            }
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate("register")
                        }
                    )
                }
                composable("register") {
                    val regContext = LocalContext.current
                    RegisterScreen(
                        onRegisterSuccess = {
                            coroutineScope.launch {
                                AppPreferences.setSavedToken(regContext, AuthTokenStore.idToken, AuthTokenStore.refreshToken)
                            }
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("home") {
                    val threatCount = remember(historyList) {
                        historyList.count { it.result.riskLevel == RiskLevel.HIGH }
                    }
                    HomeScreen(
                        threatCount = threatCount,
                        onScanModeSelected = { mode ->
                            when (mode) {
                                "chat" -> navController.navigate("scan_chat")
                                "link" -> navController.navigate("check_link")
                                "screenshot" -> navController.navigate("scan_screenshot")
                                "qr" -> navController.navigate("scan_qr")
                                else -> navController.navigate("analyzing")
                            }
                        },
                        onEducationSelected = {
                            navController.navigate("education_center") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onHistoryClick = {
                            navController.navigate("history")
                        }
                    )
                }
                composable("scan_chat") {
                    ScanChatScreen(
                        onBack = { navController.popBackStack() },
                        onAnalyze = { text ->
                            analysis.start(
                                errorPrefix = "Gagal menganalisis",
                                remindLoginOnSuccess = true
                            ) { repository.analyzeChat(text) }
                        }
                    )
                }
                composable("check_link") {
                    CheckLinkScreen(
                        onBack = { navController.popBackStack() },
                        onCheck = { url ->
                            analysis.start(
                                errorPrefix = "Gagal menganalisis link",
                                remindLoginOnSuccess = true
                            ) { repository.analyzeLink(url) }
                        }
                    )
                }
                composable("scan_screenshot") {
                    ScanScreenshotScreen(
                        onBack = { navController.popBackStack() },
                        onAnalyzeText = { text ->
                            analysis.start(errorPrefix = "Gagal menganalisis screenshot") {
                                repository.analyzeChat(text, source = "screenshot_ocr")
                            }
                        }
                    )
                }
                composable("scan_qr") {
                    ScanQRScreen(
                        onBack = { navController.popBackStack() },
                        onScanned = { data ->
                            analysis.start(errorPrefix = "Gagal menganalisis QR") {
                                repository.analyzeQr(data)
                            }
                        }
                    )
                }
                composable(
                    route = "education_detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val content = EducationCatalog.findById(id)
                    EducationDetailScreen(
                        content = content,
                        onBack = {
                            id?.let { completedEducationIds = completedEducationIds + it }
                            navController.popBackStack()
                        }
                    )
                }
                composable("education_center") {
                    EducationCenterScreen(
                        completedIds = completedEducationIds,
                        onBack = { navController.popBackStack() },
                        onItemClick = { item ->
                            if (item.type == "KUIS") {
                                navController.navigate("quiz_screen")
                            } else {
                                navController.navigate("education_detail/${item.id}")
                            }
                        }
                    )
                }
                composable("quiz_screen") {
                    QuizScreen(
                        onBack = { navController.popBackStack() },
                        onFinish = {
                            completedEducationIds = completedEducationIds + "4" // ID for Quiz
                            navController.popBackStack()
                        }
                    )
                }
                composable("history") {
                    LaunchedEffect(Unit) { fetchHistory() }
                    HistoryScreen(
                        historyList = historyList,
                        isLoading = isHistoryLoading,
                        errorMessage = historyError,
                        isLoggedIn = token != null,
                        onBack = { navController.popBackStack() },
                        onItemClick = { item ->
                            lastResult = item.result
                            resultSessionId += 1
                            navController.navigate("result") {
                                launchSingleTop = true
                            }
                        },
                        onDeleteItem = { item ->
                            coroutineScope.launch {
                                repository.deleteHistory(item.id)
                                    .onSuccess { fetchHistory() }
                                    .onFailure { showError("Gagal menghapus: ${it.message}") }
                            }
                        },
                        onRefresh = { fetchHistory() }
                    )
                }
                composable("profile") {
                    val context = LocalContext.current
                    LaunchedEffect(Unit) { fetchReportBadgeCounts() }
                    ProfileScreen(
                        userName = userDisplayName.ifEmpty {
                            userEmail.substringBefore("@").ifEmpty { "Pengguna" }
                        },
                        userEmail = userEmail,
                        scanCount = historyList.size,
                        threatCount = historyList.count { it.result.riskLevel == RiskLevel.HIGH },
                        isAdmin = isAdmin,
                        pendingMyReportsCount = pendingMyReportsCount,
                        pendingAdminReportsCount = pendingAdminReportsCount,
                        onEditProfileClick = {
                            navController.navigate("edit_profile")
                        },
                        onAdminReportsClick = {
                            coroutineScope.launch {
                                isAdminLoading = true
                                repository.getAdminReports().onSuccess { list ->
                                    adminReports.clear()
                                    adminReports.addAll(list)
                                }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
                                isAdminLoading = false
                            }
                            navController.navigate("admin_reports")
                        },
                        onMyReportsClick = {
                            fetchMyReports()
                            navController.navigate("my_reports")
                        },
                        onSecurityClick = {
                            navController.navigate("security_privacy")
                        },
                        onNotificationClick = {
                            navController.navigate("notifications")
                        },
                        onAboutClick = {
                            navController.navigate("about")
                        },
                        onHelpClick = {
                            navController.navigate("help_center")
                        },
                        onLogout = {
                            AuthTokenStore.clear()
                            historyList.clear()
                            coroutineScope.launch { AppPreferences.setSavedToken(context, null) }
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
                composable("edit_profile") {
                    EditProfileScreen(
                        initialName = userDisplayName.ifEmpty {
                            userEmail.substringBefore("@")
                        },
                        initialEmail = userEmail,
                        onBack = { navController.popBackStack() },
                        onSave = { name, email, onDone ->
                            coroutineScope.launch {
                                repository.updateProfile(name, email)
                                    .onSuccess { me ->
                                        userEmail = me.email ?: email
                                        userDisplayName = me.displayName?.takeIf { it.isNotBlank() }
                                            ?: me.email?.substringBefore("@")
                                            ?: name
                                        isAdmin = me.admin
                                        showSuccess("Profil berhasil diperbarui")
                                        onDone(Result.success(Unit))
                                    }
                                    .onFailure { onDone(Result.failure(it)) }
                            }
                        }
                    )
                }
                composable("my_reports") {
                    MyReportsScreen(
                        reports = myReports,
                        isLoading = isMyReportsLoading,
                        onBack = { navController.popBackStack() },
                        onReportClick = { report ->
                            navController.navigate("report_status/${report.reportId}")
                        },
                        onRefresh = { fetchMyReports() }
                    )
                }
                composable(
                    route = "report_status/{report_id}",
                    arguments = listOf(navArgument("report_id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val reportId = backStackEntry.arguments?.getString("report_id") ?: ""
                    LaunchedEffect(reportId) {
                        if (reportId.isNotEmpty()) {
                            isReportDetailLoading = true
                            reportDetail = null
                            repository.getMyReport(reportId).onSuccess { reportDetail = it }
                                .onFailure {
                                    showError("Gagal memuat detail: ${it.message}")
                                    reportDetail = null
                                }
                            isReportDetailLoading = false
                        }
                    }
                    ReportStatusScreen(
                        report = if (reportDetail?.reportId == reportId) reportDetail else null,
                        isLoading = isReportDetailLoading,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("admin_reports") {
                    if (!isAdmin) {
                        LaunchedEffect(Unit) {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                            }
                            showError("Akses ditolak: hanya admin yang bisa mengelola laporan.")
                        }
                    } else {
                        AdminReportsScreen(
                            reports = adminReports,
                            isLoading = isAdminLoading,
                            onBack = { navController.popBackStack() },
                            onRefresh = {
                                coroutineScope.launch {
                                    isAdminLoading = true
                                    repository.getAdminReports().onSuccess { list ->
                                        adminReports.clear()
                                        adminReports.addAll(list)
                                    }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
                                    isAdminLoading = false
                                }
                            },
                            onVerify = { reportId ->
                                coroutineScope.launch {
                                    repository.updateReportStatus(reportId, "verified").onSuccess { result ->
                                        adminReports.replaceAll {
                                            if (it.reportId == reportId) it.copy(
                                                verifiedStatus = "verified",
                                                verifiedBy = result.verifiedBy,
                                                verifiedByEmail = result.verifiedByEmail,
                                                verifiedAt = result.verifiedAt
                                            ) else it
                                        }
                                        fetchReportBadgeCounts()
                                        showSuccess("Laporan diverifikasi")
                                    }.onFailure { showError("Gagal verifikasi: ${it.message}") }
                                }
                            },
                            onReject = { reportId ->
                                coroutineScope.launch {
                                    repository.updateReportStatus(reportId, "rejected").onSuccess { result ->
                                        adminReports.replaceAll {
                                            if (it.reportId == reportId) it.copy(
                                                verifiedStatus = "rejected",
                                                verifiedBy = result.verifiedBy,
                                                verifiedByEmail = result.verifiedByEmail,
                                                verifiedAt = result.verifiedAt
                                            ) else it
                                        }
                                        fetchReportBadgeCounts()
                                        showSuccess("Laporan ditolak")
                                    }.onFailure { showError("Gagal menolak: ${it.message}") }
                                }
                            }
                        )
                    }
                }
                composable("security_privacy") {
                    SecurityPrivacyScreen(
                        onBack = { navController.popBackStack() },
                        onManagePermissions = { navController.navigate("permission_management") },
                        onChangePassword = {
                            if (token == null) {
                                showError("Login diperlukan untuk mengubah kata sandi")
                            } else {
                                navController.navigate("change_password")
                            }
                        },
                        onAutoCleanEnabled = {
                            coroutineScope.launch {
                                if (token == null) {
                                    showError("Login diperlukan untuk membersihkan riwayat")
                                    return@launch
                                }
                                val deleted = HistoryAutoCleaner.cleanOldHistory(repository)
                                fetchHistory()
                                if (deleted > 0) {
                                    showSuccess("Pembersihan otomatis: $deleted riwayat lama dihapus")
                                }
                            }
                        }
                    )
                }
                composable("change_password") {
                    val pwdContext = LocalContext.current
                    ChangePasswordScreen(
                        onBack = { navController.popBackStack() },
                        onSubmit = { currentPassword, newPassword, onDone ->
                            coroutineScope.launch {
                                repository.changePassword(currentPassword, newPassword)
                                    .onSuccess { tokens ->
                                        AppPreferences.setSavedToken(
                                            pwdContext,
                                            tokens.idToken,
                                            tokens.refreshToken
                                        )
                                        onDone(Result.success(Unit))
                                        showSuccess("Kata sandi berhasil diubah")
                                        navController.popBackStack()
                                    }
                                    .onFailure { err ->
                                        onDone(Result.failure(err))
                                    }
                            }
                        }
                    )
                }
                composable("permission_management") {
                    PermissionManagementScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("notifications") {
                    LaunchedEffect(Unit) { fetchNotifications() }
                    NotificationScreen(
                        notifications = notificationList,
                        isLoading = isNotificationsLoading,
                        errorMessage = notificationsError,
                        onBack = { navController.popBackStack() },
                        onRefresh = { fetchNotifications() },
                        onNotificationClick = { item ->
                            coroutineScope.launch {
                                repository.markNotificationRead(item.id)
                                fetchNotifications()
                            }
                            val reportId = item.data?.get("report_id")?.toString()
                            if (!reportId.isNullOrBlank() &&
                                (item.type == "report_status_updated" || item.type == "new_report")
                            ) {
                                if (item.type == "new_report" && isAdmin) {
                                    navController.navigate("admin_reports")
                                } else {
                                    navController.navigate("report_status/$reportId")
                                }
                            }
                        }
                    )
                }
                composable("about") {
                    AboutScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("help_center") {
                    HelpCenterScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("block_delete") {
                    BlockDeleteScreen(
                        onBackToHome = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
                composable("report") {
                    ReportScreen(
                        onBack = { navController.popBackStack() },
                        onSubmit = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        onSubmitReport = { type, content, note, onResult ->
                            coroutineScope.launch {
                                repository.submitReport(type, content, note)
                                    .onSuccess {
                                        fetchReportBadgeCounts()
                                        fetchMyReports()
                                        onResult(true, null)
                                    }
                                    .onFailure { err ->
                                        onResult(false, err.message ?: "Gagal mengirim laporan")
                                    }
                            }
                        }
                    )
                }
                composable("analyzing") {
                    AnalyzingOverlay()
                }
                composable("result") {
                    val result = lastResult ?: ScanResult(
                        type = "unknown",
                        riskScore = 0,
                        riskLevel = RiskLevel.LOW,
                        inputSummary = "",
                        flags = emptyList<String>(),
                        explanation = "Tidak ada hasil analisis.",
                        recommendation = "Silakan lakukan scan terlebih dahulu."
                    )
                    val relatedEducation = remember(result.relatedArticle, result.riskLevel, resultSessionId) {
                        val shouldOffer = result.riskLevel != RiskLevel.LOW ||
                            !result.relatedArticle.isNullOrBlank()
                        if (!shouldOffer) null
                        else EducationMatcher.findByCategory(
                            category = result.relatedArticle,
                            contents = educationContents,
                            fallbackToFeatured = result.riskLevel != RiskLevel.LOW,
                        )
                    }
                    key(resultSessionId, result.inputSummary, result.riskScore) {
                        ResultScreen(
                            result = result,
                            onBackToHome = {
                                navController.navigate("home") {
                                    popUpTo("result") { inclusive = true }
                                }
                            },
                            onHistoryClick = {
                                navController.navigate("history")
                            },
                            onBlockDeleteClick = {
                                navController.navigate("block_delete")
                            },
                            onReportClick = {
                                navController.navigate("report")
                            },
                            learnMoreTitle = relatedEducation?.title,
                            onLearnMoreClick = relatedEducation?.let { content ->
                                {
                                    navController.navigate("education_detail/${content.id}")
                                }
                            }
                        )
                    }
                }
            }

            // Floating Navigation Bar
            if (showNavBar) {
                FloatingNavBar(
                    items = navItems,
                    currentRoute = selectedRoute,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
