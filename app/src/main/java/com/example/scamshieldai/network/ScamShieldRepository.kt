package com.example.scamshieldai.network

import com.example.scamshieldai.auth.AuthTokenStore

class ScamShieldRepository {

    private val api = ApiClient.api

    private suspend fun getAuthToken(): String? {
        return AuthTokenStore.idToken?.let { "Bearer $it" }
    }

    suspend fun getMe(): Result<AuthMeData> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.getMe(token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat profil")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat profil")))
        }
    }

    suspend fun updateProfile(displayName: String?, email: String?): Result<AuthMeData> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.updateProfile(
                UpdateProfileRequest(displayName = displayName, email = email),
                token
            )
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memperbarui profil")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memperbarui profil")))
        }
    }

<<<<<<< HEAD
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<String> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.changePassword(
                ChangePasswordRequest(currentPassword = currentPassword, newPassword = newPassword),
                token
            )
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.message)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal mengubah password")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal mengubah password")))
=======
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<AuthTokens> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.changePassword(
                ChangePasswordRequest(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                ),
                token
            )
            if (response.isSuccessful && response.body()?.success == true) {
                val tokens = response.body()!!.data
                AuthTokenStore.setToken(tokens.idToken, tokens.refreshToken)
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal mengubah kata sandi")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal mengubah kata sandi")))
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
        }
    }

    suspend fun login(email: String, password: String): Result<AuthTokens> {
        return try {
            val response = api.login(AuthLoginRequest(email = email, password = password))
            if (response.isSuccessful && response.body()?.success == true) {
                val tokens = response.body()!!.data
                AuthTokenStore.setToken(tokens.idToken, tokens.refreshToken)
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal login")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal login")))
        }
    }

    suspend fun register(email: String, password: String): Result<AuthTokens> {
        return try {
            val response = api.register(AuthRegisterRequest(email = email, password = password))
            if (response.isSuccessful && response.body()?.success == true) {
                val tokens = response.body()!!.data
                AuthTokenStore.setToken(tokens.idToken, tokens.refreshToken)
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal register")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal register")))
        }
    }

    suspend fun googleLogin(googleIdToken: String): Result<AuthTokens> {
        return try {
            val response = api.googleLogin(AuthGoogleRequest(idToken = googleIdToken))
            if (response.isSuccessful && response.body()?.success == true) {
                val tokens = response.body()!!.data
                AuthTokenStore.setToken(tokens.idToken, tokens.refreshToken)
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Google login gagal")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Google login gagal")))
        }
    }

    suspend fun analyzeChat(text: String, source: String? = null): Result<AnalysisResult> {
        return try {
            val token = getAuthToken()
            val response = api.analyzeChat(AnalyzeChatRequest(text, source), token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal menganalisis chat"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyzeLink(url: String, contextText: String? = null): Result<AnalysisResult> {
        return try {
            val token = getAuthToken()
            val response = api.analyzeLink(AnalyzeLinkRequest(url, contextText), token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal menganalisis link"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyzeQr(decodedContent: String): Result<AnalysisResult> {
        return try {
            val token = getAuthToken()
            val response = api.analyzeQr(AnalyzeQrRequest(decodedContent), token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal menganalisis QR"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistory(limit: Int = 20, cursor: String? = null): Result<HistoryListResponse> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.getHistory(limit, cursor, token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat riwayat")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat riwayat")))
        }
    }

    suspend fun deleteHistory(scanId: String): Result<HistoryDeleteResponse> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.deleteHistory(scanId, token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal menghapus riwayat")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal menghapus riwayat")))
        }
    }

    suspend fun submitReport(type: String, content: String, note: String? = null): Result<CommunityReportResult> {
        return try {
            val token = getAuthToken()
            val response = api.submitReport(CommunityReportRequest(type, content, note), token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal mengirim laporan")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal mengirim laporan")))
        }
    }

    suspend fun getMyReports(): Result<List<UserReportItem>> {
        val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
        return try {
            val response = api.getMyReports(token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat laporan")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat laporan")))
        }
    }

    suspend fun getMyReport(reportId: String): Result<UserReportItem> {
        val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
        return try {
            val response = api.getMyReport(reportId, token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat detail laporan")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat detail laporan")))
        }
    }

    suspend fun getMyPendingReportCount(): Result<Int> {
        val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
        return try {
            val response = api.getMyReportCount(statusFilter = "pending", token = token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.count)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat jumlah laporan")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat jumlah laporan")))
        }
    }

    suspend fun getAdminPendingReportCount(): Result<Int> {
        val token = getAuthToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = api.getAdminReportCount(statusFilter = "pending", token = token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.count)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat jumlah laporan")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat jumlah laporan")))
        }
    }

    suspend fun getEducationList(category: String? = null): Result<List<EducationContentSummary>> {
        return try {
            val response = api.getEducationList(category)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal memuat konten edukasi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEducationDetail(contentId: String): Result<EducationContentDetail> {
        return try {
            val response = api.getEducationDetail(contentId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal memuat detail edukasi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAdminReports(statusFilter: String? = null): Result<List<AdminReportItem>> {
        val token = getAuthToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = api.getAdminReports(statusFilter = statusFilter, token = token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat laporan")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat laporan")))
        }
    }

    suspend fun updateReportStatus(reportId: String, status: String): Result<UpdateReportStatusResponse> {
        val token = getAuthToken() ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = api.updateReportStatus(reportId, UpdateReportStatusRequest(status), token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal mengubah status")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal mengubah status")))
        }
    }

    suspend fun registerFcmToken(fcmToken: String): Result<Unit> {
        val token = AuthTokenStore.idToken ?: return Result.failure(Exception("Not authenticated"))
        return try {
            val response = api.registerFcmToken(RegisterFcmTokenRequest(fcmToken), "Bearer $token")
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.apiErrorMessage("Gagal mendaftarkan token notifikasi")))
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal mendaftarkan token notifikasi")))
        }
    }

    suspend fun getNotifications(): Result<List<NotificationItem>> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.getNotifications(token)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.apiErrorMessage("Gagal memuat notifikasi")))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memuat notifikasi")))
        }
    }

    suspend fun markNotificationRead(notifId: String): Result<Unit> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("Belum login"))
            val response = api.markNotificationRead(notifId, token)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.apiErrorMessage("Gagal memperbarui notifikasi")))
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserMessage("Gagal memperbarui notifikasi")))
        }
    }
}
