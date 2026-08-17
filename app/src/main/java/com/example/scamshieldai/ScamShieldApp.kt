package com.example.scamshieldai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scamshieldai.analysis.AnalysisCoordinator
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.ui.components.AnimatedNavBar
import com.example.scamshieldai.ui.feedback.AppSnackbarHost
import com.example.scamshieldai.ui.navigation.NavigationItems
import com.example.scamshieldai.ui.navigation.ScamShieldNavGraph
import com.example.scamshieldai.ui.navigation.Screen
import com.example.scamshieldai.ui.state.rememberScamShieldAppState

@Composable
fun ScamShieldApp(
    pendingNotificationRoute: String? = null,
    onNotificationRouteConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()
    val appState = rememberScamShieldAppState()
    val token = AuthTokenStore.idToken

    // Restore saved session on launch
    LaunchedEffect(Unit) {
        val restored = appState.restoreSavedSession()
        if (restored) {
            navController.navigate(Screen.Home) {
                popUpTo(Screen.Login) { inclusive = true }
            }
        }
    }

    // React to auth token changes (profile, fcm, notifications, history)
    LaunchedEffect(token) {
        appState.onTokenChanged(token)
    }

    // Handle deep links from FCM push notifications
    LaunchedEffect(pendingNotificationRoute, token, appState.isAdmin) {
        val route = pendingNotificationRoute ?: return@LaunchedEffect
        when {
            route.startsWith("report_status/") -> {
                if (token == null) {
                    appState.showError("Silakan login untuk melihat laporan.")
                } else {
                    val reportId = route.removePrefix("report_status/")
                    navController.navigate(Screen.reportStatusRoute(reportId))
                }
            }
            route == Screen.AdminReports -> {
                if (!appState.isAdmin) {
                    appState.showError("Akses ditolak: hanya admin.")
                } else {
                    appState.fetchAdminReports()
                    navController.navigate(Screen.AdminReports)
                }
            }
        }
        onNotificationRouteConsumed()
    }

    // Analysis coordinator for async scanning flows
    val analysis = remember {
        AnalysisCoordinator(
            scope = appState.scope,
            navController = navController,
            onClearPendingResult = { appState.lastResult = null },
            onResultReady = { result ->
                appState.lastResult = result
                appState.resultSessionId += 1
            },
            onFetchHistory = { appState.fetchHistory() },
            showError = { appState.showError(it) },
            showInfo = { appState.showInfo(it) },
            isLoggedIn = { appState.isLoggedIn }
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home
    val selectedRoute = NavigationItems.getSelectedRoute(currentRoute)
    val showNavBar = NavigationItems.shouldShowNavBar(currentRoute)
    val navItems = NavigationItems.buildBottomNavItems(appState.profileBadgeCount)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { AppSnackbarHost(appState.snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            ScamShieldNavGraph(
                navController = navController,
                appState = appState,
                analysisCoordinator = analysis,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            if (showNavBar) {
                AnimatedNavBar(
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