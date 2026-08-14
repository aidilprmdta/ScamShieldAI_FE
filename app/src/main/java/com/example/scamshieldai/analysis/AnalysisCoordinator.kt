package com.example.scamshieldai.analysis

import androidx.navigation.NavController
import com.example.scamshieldai.ui.screens.ScanResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.scamshieldai.network.AnalysisResult as ApiAnalysisResult

/**
 * Menjalankan analisis API dengan pembatalan request lama dan navigasi analyzing → result.
 */
class AnalysisCoordinator(
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val onClearPendingResult: () -> Unit,
    private val onResultReady: (ScanResult) -> Unit,
    private val onFetchHistory: () -> Unit,
    private val showError: (String) -> Unit,
    private val showInfo: (String) -> Unit,
    private val isLoggedIn: () -> Boolean
) {
    private var job: Job? = null
    private var requestId: Int = 0

    fun start(
        errorPrefix: String,
        remindLoginOnSuccess: Boolean = false,
        analyze: suspend () -> Result<ApiAnalysisResult>
    ) {
        job?.cancel()
        onClearPendingResult()
        val id = ++requestId

        navController.navigate("analyzing") {
            launchSingleTop = true
        }

        job = scope.launch {
            try {
                analyze()
                    .onSuccess { apiResult ->
                        if (id != requestId) return@onSuccess
                        onResultReady(mapApiResultToScanResult(apiResult))
                        navController.navigate("result") {
                            popUpTo("analyzing") { inclusive = true }
                            launchSingleTop = true
                        }
                        onFetchHistory()
                        if (remindLoginOnSuccess && !isLoggedIn()) {
                            showInfo("Login agar hasil scan tersimpan di Riwayat")
                        }
                    }
                    .onFailure { err ->
                        if (id != requestId) return@onFailure
                        showError("$errorPrefix: ${err.message}")
                        if (navController.currentDestination?.route == "analyzing") {
                            navController.popBackStack()
                        }
                    }
            } catch (_: CancellationException) {
                // Diganti analisis baru
            }
        }
    }
}
