package com.example.scamshieldai.network

import retrofit2.Response
import retrofit2.http.*

interface ScamShieldApi {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: AuthLoginRequest
    ): Response<AuthResponse>

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: AuthRegisterRequest
    ): Response<AuthResponse>

    @POST("api/v1/auth/google")
    suspend fun googleLogin(
        @Body request: AuthGoogleRequest
    ): Response<AuthResponse>

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(
        @Body request: AuthRefreshRequest
    ): Response<AuthResponse>

    @GET("api/v1/auth/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<AuthMeResponse>

    @POST("api/v1/analyze/chat")
    suspend fun analyzeChat(
        @Body request: AnalyzeChatRequest,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<AnalysisResult>>

    @POST("api/v1/analyze/link")
    suspend fun analyzeLink(
        @Body request: AnalyzeLinkRequest,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<AnalysisResult>>

    @POST("api/v1/analyze/qr")
    suspend fun analyzeQr(
        @Body request: AnalyzeQrRequest,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<AnalysisResult>>

    @GET("api/v1/history")
    suspend fun getHistory(
        @Query("limit") limit: Int = 20,
        @Query("cursor") cursor: String? = null,
        @Header("Authorization") token: String
    ): Response<HistoryListResponse>

    @DELETE("api/v1/history/{scan_id}")
    suspend fun deleteHistory(
        @Path("scan_id") scanId: String,
        @Header("Authorization") token: String
    ): Response<HistoryDeleteResponse>

    @POST("api/v1/report")
    suspend fun submitReport(
        @Body request: CommunityReportRequest,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<CommunityReportResult>>

    @GET("api/v1/reports/mine")
    suspend fun getMyReports(
        @Header("Authorization") token: String
    ): Response<UserReportListResponse>

    @GET("api/v1/reports/mine/count")
    suspend fun getMyReportCount(
        @Query("status_filter") statusFilter: String? = "pending",
        @Header("Authorization") token: String
    ): Response<ReportCountResponse>

    @GET("api/v1/reports/{report_id}")
    suspend fun getMyReport(
        @Path("report_id") reportId: String,
        @Header("Authorization") token: String
    ): Response<UserReportDetailResponse>

    @GET("api/v1/education")
    suspend fun getEducationList(
        @Query("category") category: String? = null
    ): Response<ApiResponse<List<EducationContentSummary>>>

    @GET("api/v1/education/{content_id}")
    suspend fun getEducationDetail(
        @Path("content_id") contentId: String
    ): Response<ApiResponse<EducationContentDetail>>

    @GET("api/v1/admin/reports")
    suspend fun getAdminReports(
        @Query("status_filter") statusFilter: String? = null,
        @Query("limit") limit: Int = 50,
        @Header("Authorization") token: String
    ): Response<AdminReportListResponse>

    @GET("api/v1/admin/reports/count")
    suspend fun getAdminReportCount(
        @Query("status_filter") statusFilter: String? = "pending",
        @Header("Authorization") token: String
    ): Response<ReportCountResponse>

    @PATCH("api/v1/admin/reports/{report_id}")
    suspend fun updateReportStatus(
        @Path("report_id") reportId: String,
        @Body request: UpdateReportStatusRequest,
        @Header("Authorization") token: String
    ): Response<UpdateReportStatusResponse>

    @POST("api/v1/notifications/register-token")
    suspend fun registerFcmToken(
        @Body request: RegisterFcmTokenRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Any>>
}
