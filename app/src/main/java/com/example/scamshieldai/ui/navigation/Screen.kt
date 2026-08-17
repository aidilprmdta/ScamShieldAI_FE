package com.example.scamshieldai.ui.navigation

/**
 * Daftar rute navigasi yang digunakan di seluruh aplikasi ScamShield AI.
 */
object Screen {
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val EducationCenter = "education_center"
    const val EducationDetail = "education_detail/{id}"
    const val Quiz = "quiz_screen"
    const val History = "history"
    const val Profile = "profile"
    const val EditProfile = "edit_profile"
    const val SecurityPrivacy = "security_privacy"
    const val ChangePassword = "change_password"
    const val PermissionManagement = "permission_management"
    const val Notifications = "notifications"
    const val About = "about"
    const val HelpCenter = "help_center"
    const val ScanChat = "scan_chat"
    const val CheckLink = "check_link"
    const val ScanScreenshot = "scan_screenshot"
    const val ScanQR = "scan_qr"
    const val Analyzing = "analyzing"
    const val Result = "result"
    const val BlockDelete = "block_delete"
    const val Report = "report"
    const val MyReports = "my_reports"
    const val ReportStatus = "report_status/{report_id}"
    const val AdminReports = "admin_reports"

    fun educationDetailRoute(contentId: String): String = "education_detail/$contentId"
    fun reportStatusRoute(reportId: String): String = "report_status/$reportId"
}
