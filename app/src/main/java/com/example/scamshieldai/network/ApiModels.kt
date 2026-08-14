package com.example.scamshieldai.network

import com.google.gson.annotations.SerializedName

// ===== REQUEST MODELS =====

data class AnalyzeChatRequest(
    val text: String,
    val source: String? = null,
    @SerializedName("ocr_confidence") val ocrConfidence: Float? = null
)

data class AnalyzeLinkRequest(
    val url: String,
    @SerializedName("context_text") val contextText: String? = null
)

data class AnalyzeQrRequest(
    @SerializedName("decoded_content") val decodedContent: String
)

data class CommunityReportRequest(
    val type: String,
    val content: String,
    val note: String? = null
)

// ===== RESPONSE MODELS =====

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)

data class AnalysisResult(
    @SerializedName("scan_id") val scanId: String?,
    val type: String,
    @SerializedName("input_summary") val inputSummary: String,
    @SerializedName("risk_score") val riskScore: Int,
    @SerializedName("risk_level") val riskLevel: String,
    val explanation: String,
    @SerializedName("red_flags") val redFlags: List<RedFlag>?,
    val recommendation: String,
    @SerializedName("recommendation_text") val recommendationText: String?,
    @SerializedName("related_education_category") val relatedEducationCategory: String?,
    @SerializedName("link_reputation") val linkReputation: Map<String, Any>?,
    @SerializedName("created_at") val createdAt: String?
)

data class RedFlag(
    val label: String,
    val detail: String
)

data class HistoryListResponse(
    val success: Boolean,
    val data: List<AnalysisResult>,
    @SerializedName("next_cursor") val nextCursor: String?,
    val message: String? = null
)

data class HistoryDeleteResponse(
    val success: Boolean,
    val message: String
)

data class CommunityReportResult(
    @SerializedName("report_id") val reportId: String,
    val type: String,
    val content: String,
    val note: String?,
    @SerializedName("verified_status") val verifiedStatus: String,
    @SerializedName("reported_by") val reportedBy: String?,
    @SerializedName("created_at") val createdAt: String
)

data class EducationContentSummary(
    val id: String,
    val title: String,
    val category: String,
    val type: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("published_at") val publishedAt: String?
)

data class EducationContentDetail(
    val id: String,
    val title: String,
    val category: String,
    val type: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("published_at") val publishedAt: String?,
    val body: String?,
    @SerializedName("quiz_questions") val quizQuestions: List<QuizQuestion>?
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    @SerializedName("correct_index") val correctIndex: Int,
    val explanation: String?
)

// ===== AUTH (FE <-> BE) =====

data class AuthLoginRequest(
    val email: String,
    val password: String
)

data class AuthRegisterRequest(
    val email: String,
    val password: String
)

data class AuthTokens(
    val idToken: String,
    val refreshToken: String,
    val localId: String,
    val email: String? = null
)

data class AuthGoogleRequest(
    @SerializedName("id_token") val idToken: String
)

data class AuthRefreshRequest(
    @SerializedName("refresh_token") val refreshToken: String
)

data class AuthResponse(
    val success: Boolean,
    val data: AuthTokens,
    val message: String? = null
)

data class AuthMeData(
    val uid: String,
    val email: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    val admin: Boolean = false
)

data class AuthMeResponse(
    val success: Boolean,
    val data: AuthMeData,
    val message: String? = null
)

data class UpdateProfileRequest(
    @SerializedName("display_name") val displayName: String? = null,
    val email: String? = null
)

data class UpdateProfileResponse(
    val success: Boolean,
    val data: AuthMeData,
    val message: String? = null
)

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String
)

data class ChangePasswordResponse(
    val success: Boolean,
    val data: AuthTokens,
    val message: String? = null
)

// ===== ADMIN =====

data class AdminReportItem(
    @SerializedName("report_id") val reportId: String,
    val type: String,
    val content: String,
    val note: String? = null,
    @SerializedName("verified_status") val verifiedStatus: String,
    @SerializedName("reported_by") val reportedBy: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("verified_by") val verifiedBy: String? = null,
    @SerializedName("verified_by_email") val verifiedByEmail: String? = null,
    @SerializedName("verified_at") val verifiedAt: String? = null
)

data class AdminReportListResponse(
    val success: Boolean,
    val data: List<AdminReportItem>,
    val total: Int,
    val message: String? = null
)

data class UpdateReportStatusRequest(
    val status: String
)

data class UpdateReportStatusResponse(
    val success: Boolean,
    val message: String? = null,
    @SerializedName("verified_by") val verifiedBy: String? = null,
    @SerializedName("verified_by_email") val verifiedByEmail: String? = null,
    @SerializedName("verified_at") val verifiedAt: String? = null
)

// ===== NOTIFICATIONS =====

data class RegisterFcmTokenRequest(
    @SerializedName("fcm_token") val fcmToken: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val type: String,
    val read: Boolean = false,
    @SerializedName("created_at") val createdAt: String = "",
    val data: Map<String, Any>? = null
)

data class NotificationListResponse(
    val success: Boolean,
    val data: List<NotificationItem> = emptyList(),
    val message: String? = null
)

// ===== USER REPORTS =====

data class UserReportItem(
    @SerializedName("report_id") val reportId: String,
    val type: String,
    val content: String,
    val note: String? = null,
    @SerializedName("verified_status") val verifiedStatus: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("verified_at") val verifiedAt: String? = null
)

data class UserReportListResponse(
    val success: Boolean,
    val data: List<UserReportItem>,
    val message: String? = null
)

data class UserReportDetailResponse(
    val success: Boolean,
    val data: UserReportItem,
    val message: String? = null
)

data class ReportCountResponse(
    val success: Boolean,
    val count: Int,
    val message: String? = null
)


// ===== CHANGE PASSWORD =====

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String
)

data class ChangePasswordResponse(
    val success: Boolean,
    val message: String
)
