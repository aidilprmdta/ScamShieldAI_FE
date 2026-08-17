package com.example.scamshieldai.ui.state

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.scamshieldai.analysis.AnalysisCoordinator
import com.example.scamshieldai.analysis.mapApiResultToScanResult
import com.example.scamshieldai.auth.AuthSession
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.network.*
import com.example.scamshieldai.settings.AppPreferences
import com.example.scamshieldai.settings.HistoryAutoCleaner
import com.example.scamshieldai.ui.feedback.showError
import com.example.scamshieldai.ui.feedback.showInfo
import com.example.scamshieldai.ui.feedback.showSuccess
import com.example.scamshieldai.ui.screens.HistoryItem
import com.example.scamshieldai.ui.screens.RiskLevel
import com.example.scamshieldai.ui.screens.ScanResult
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * State Holder utama untuk mengelola state bisnis & UI di ScamShield AI.
 */
class ScamShieldAppState(
    val repository: ScamShieldRepository,
    val scope: CoroutineScope,
    val context: Context,
    val snackbarHostState: SnackbarHostState
) {
    // Session & User
    var userEmail by mutableStateOf("")
        private set
    var userDisplayName by mutableStateOf("")
        private set
    var isAdmin by mutableStateOf(false)
        private set
    val isLoggedIn: Boolean
        get() = AuthTokenStore.idToken != null

    // Scan & Result State
    var lastResult by mutableStateOf<ScanResult?>(null)
    var resultSessionId by mutableIntStateOf(0)

    // History
    val historyList: SnapshotStateList<HistoryItem> = mutableStateListOf()
    var isHistoryLoading by mutableStateOf(false)
        private set
    var historyError by mutableStateOf<String?>(null)
        private set

    // Reports
    val myReports: SnapshotStateList<UserReportItem> = mutableStateListOf()
    var isMyReportsLoading by mutableStateOf(false)
        private set
    var reportDetail by mutableStateOf<UserReportItem?>(null)
        private set
    var isReportDetailLoading by mutableStateOf(false)
        private set

    val adminReports: SnapshotStateList<AdminReportItem> = mutableStateListOf()
    var isAdminLoading by mutableStateOf(false)
        private set

    var pendingMyReportsCount by mutableIntStateOf(0)
        private set
    var pendingAdminReportsCount by mutableIntStateOf(0)
        private set

    // Notifications
    val notificationList: SnapshotStateList<NotificationItem> = mutableStateListOf()
    var isNotificationsLoading by mutableStateOf(false)
        private set
    var notificationsError by mutableStateOf<String?>(null)
        private set

    val threatCount: Int
        get() = historyList.count { it.result.riskLevel == RiskLevel.HIGH }

    val profileBadgeCount: Int
        get() = if (isAdmin) pendingAdminReportsCount else pendingMyReportsCount

    // ===== UI Feedback =====
    fun showError(message: String) {
        scope.launch { snackbarHostState.showError(message) }
    }

    fun showSuccess(message: String) {
        scope.launch { snackbarHostState.showSuccess(message) }
    }

    fun showInfo(message: String) {
        scope.launch { snackbarHostState.showInfo(message) }
    }

    // ===== Business Logic =====

    suspend fun restoreSavedSession(): Boolean {
        val savedToken = AppPreferences.savedTokenFlow(context).first()
        val savedRefresh = AppPreferences.savedRefreshTokenFlow(context).first()
        if (savedToken != null || savedRefresh != null) {
            AuthTokenStore.restore(savedToken, savedRefresh)
            val sessionOk = AuthSession.ensureValidToken()
            if (sessionOk) {
                return true
            } else {
                AuthTokenStore.clear()
            }
        }
        return false
    }

    fun onTokenChanged(token: String?) {
        fetchHistory()
        if (token != null) {
            scope.launch {
                repository.getMe().onSuccess { me ->
                    userEmail = me.email ?: ""
                    userDisplayName = me.displayName?.takeIf { it.isNotBlank() }
                        ?: me.email?.substringBefore("@").orEmpty()
                    isAdmin = me.admin
                    fetchBadgeCounts()
                }
            }
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { fcmToken: String ->
                    scope.launch { repository.registerFcmToken(fcmToken) }
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

    fun fetchHistory() {
        scope.launch {
            isHistoryLoading = true
            historyError = null

            val autoClean = AppPreferences.autoCleanEnabledFlow(context).first()
            if (autoClean && isLoggedIn) {
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

    fun deleteHistoryItem(item: HistoryItem) {
        scope.launch {
            repository.deleteHistory(item.id)
                .onSuccess { fetchHistory() }
                .onFailure { showError("Gagal menghapus: ${it.message}") }
        }
    }

    fun fetchBadgeCounts() {
        scope.launch {
            if (!isLoggedIn) {
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

    fun fetchMyReports() {
        scope.launch {
            isMyReportsLoading = true
            repository.getMyReports().onSuccess { list ->
                myReports.clear()
                myReports.addAll(list)
            }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
            isMyReportsLoading = false
        }
    }

    fun fetchReportDetail(reportId: String) {
        if (reportId.isEmpty()) return
        scope.launch {
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

    fun fetchAdminReports() {
        scope.launch {
            isAdminLoading = true
            repository.getAdminReports().onSuccess { list ->
                adminReports.clear()
                adminReports.addAll(list)
            }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
            isAdminLoading = false
        }
    }

    fun verifyAdminReport(reportId: String) {
        scope.launch {
            repository.updateReportStatus(reportId, "verified").onSuccess { result ->
                val index = adminReports.indexOfFirst { it.reportId == reportId }
                if (index != -1) {
                    adminReports[index] = adminReports[index].copy(
                        verifiedStatus = "verified",
                        verifiedBy = result.verifiedBy,
                        verifiedByEmail = result.verifiedByEmail,
                        verifiedAt = result.verifiedAt
                    )
                }
                fetchBadgeCounts()
                showSuccess("Laporan diverifikasi")
            }.onFailure { showError("Gagal verifikasi: ${it.message}") }
        }
    }

    fun rejectAdminReport(reportId: String) {
        scope.launch {
            repository.updateReportStatus(reportId, "rejected").onSuccess { result ->
                val index = adminReports.indexOfFirst { it.reportId == reportId }
                if (index != -1) {
                    adminReports[index] = adminReports[index].copy(
                        verifiedStatus = "rejected",
                        verifiedBy = result.verifiedBy,
                        verifiedByEmail = result.verifiedByEmail,
                        verifiedAt = result.verifiedAt
                    )
                }
                fetchBadgeCounts()
                showSuccess("Laporan ditolak")
            }.onFailure { showError("Gagal menolak: ${it.message}") }
        }
    }

    fun fetchNotifications() {
        scope.launch {
            if (!isLoggedIn) {
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

    fun markNotificationRead(notifId: String) {
        scope.launch {
            repository.markNotificationRead(notifId)
            fetchNotifications()
        }
    }

    fun updateProfile(name: String, email: String, onDone: (Result<Unit>) -> Unit) {
        scope.launch {
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

    fun changePassword(current: String, new: String, onDone: (Result<Unit>) -> Unit) {
        scope.launch {
            repository.changePassword(current, new)
                .onSuccess { tokens ->
                    AppPreferences.setSavedToken(context, tokens.idToken, tokens.refreshToken)
                    showSuccess("Kata sandi berhasil diubah")
                    onDone(Result.success(Unit))
                }
                .onFailure { err ->
                    onDone(Result.failure(err))
                }
        }
    }

    fun submitReport(type: String, content: String, note: String?, onResult: (Boolean, String?) -> Unit) {
        scope.launch {
            repository.submitReport(type, content, note)
                .onSuccess {
                    fetchBadgeCounts()
                    fetchMyReports()
                    onResult(true, null)
                }
                .onFailure { err ->
                    onResult(false, err.message ?: "Gagal mengirim laporan")
                }
        }
    }

    fun autoCleanHistory() {
        scope.launch {
            if (!isLoggedIn) {
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

    fun logout() {
        AuthTokenStore.clear()
        historyList.clear()
        scope.launch { AppPreferences.setSavedToken(context, null) }
    }
}

@Composable
fun rememberScamShieldAppState(
    repository: ScamShieldRepository = remember { ScamShieldRepository() },
    scope: CoroutineScope = rememberCoroutineScope(),
    context: Context = androidx.compose.ui.platform.LocalContext.current,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): ScamShieldAppState {
    return remember(repository, scope, context, snackbarHostState) {
        ScamShieldAppState(
            repository = repository,
            scope = scope,
            context = context,
            snackbarHostState = snackbarHostState
        )
    }
}
