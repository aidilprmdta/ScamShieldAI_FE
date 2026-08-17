package com.example.scamshieldai.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data model for items in the [AnimatedNavBar].
 */
data class NavigationItemData(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val title: String,
    val badgeCount: Int = 0
)
