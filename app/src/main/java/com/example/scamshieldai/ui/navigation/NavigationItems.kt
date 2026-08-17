package com.example.scamshieldai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import com.example.scamshieldai.ui.components.NavigationItemData

/**
 * Konfigurasi navigasi bawah (bottom navigation bar).
 */
object NavigationItems {

    private val HIDE_NAV_BAR_PREFIXES = listOf(
        Screen.Login,
        Screen.Register,
        Screen.ScanChat,
        Screen.CheckLink,
        Screen.ScanScreenshot,
        Screen.ScanQR,
        Screen.Analyzing,
        Screen.Result,
        Screen.BlockDelete,
        Screen.Report,
        Screen.Quiz,
        "education_detail",
        Screen.SecurityPrivacy,
        Screen.ChangePassword,
        Screen.PermissionManagement,
        Screen.Notifications,
        Screen.About,
        Screen.AdminReports,
        Screen.MyReports,
        "report_status",
        Screen.EditProfile,
        Screen.HelpCenter
    )

    fun shouldShowNavBar(currentRoute: String?): Boolean {
        if (currentRoute == null) return true
        return !HIDE_NAV_BAR_PREFIXES.any { currentRoute.startsWith(it) }
    }

    fun getSelectedRoute(currentRoute: String?): String {
        val route = currentRoute ?: Screen.Home
        return if (route.startsWith("education_detail")) Screen.EducationCenter else route
    }

    fun buildBottomNavItems(badgeCount: Int): List<NavigationItemData> {
        return listOf(
            NavigationItemData(
                route = Screen.Home,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                title = "Beranda"
            ),
            NavigationItemData(
                route = Screen.EducationCenter,
                selectedIcon = Icons.Filled.School,
                unselectedIcon = Icons.Outlined.School,
                title = "Edukasi"
            ),
            NavigationItemData(
                route = Screen.History,
                selectedIcon = Icons.Filled.History,
                unselectedIcon = Icons.Outlined.History,
                title = "Riwayat"
            ),
            NavigationItemData(
                route = Screen.Profile,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                title = "Profil",
                badgeCount = badgeCount
            )
        )
    }
}
