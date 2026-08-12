package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

@Composable
fun ProfileScreen(
    userName: String = "Pengguna ScamShield",
    userEmail: String = "",
    scanCount: Int = 0,
    threatCount: Int = 0,
    isDarkMode: Boolean = false,
    isBiometricEnabled: Boolean = false,
    isAdmin: Boolean = false,
    pendingMyReportsCount: Int = 0,
    pendingAdminReportsCount: Int = 0,
    onDarkModeToggle: (Boolean) -> Unit = {},
    onBiometricToggle: (Boolean) -> Unit = {},
    onSecurityClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onAboutClick: () -> Unit,
    onMyReportsClick: () -> Unit = {},
    onAdminReportsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // Hero Section with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(Brush.verticalGradient(listOf(DeepNavy, YaleBlue)))
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Profil",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Enhanced Avatar
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Cerulean.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Cerulean,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = "Premium",
                        tint = Cerulean,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = userEmail,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            // Stats Row
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatItem(
                    icon = Icons.Outlined.Shield,
                    value = threatCount.toString(),
                    label = "Ancaman",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    icon = Icons.Outlined.History,
                    value = scanCount.toString(),
                    label = "Scan",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isAdmin) {
                ProfileSection(title = "ADMIN") {
                    ProfileItem(
                        icon = Icons.Default.AdminPanelSettings,
                        label = "Kelola Laporan",
                        iconTint = DangerRed,
                        badgeCount = pendingAdminReportsCount,
                        onClick = onAdminReportsClick
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Settings Group
            ProfileSection(title = "PENGATURAN AKUN") {
                ProfileItem(
                    icon = Icons.Default.Campaign,
                    label = "Laporan Saya",
                    iconTint = Cerulean,
                    badgeCount = pendingMyReportsCount,
                    onClick = onMyReportsClick
                )
                ProfileItem(
                    icon = Icons.Default.Lock,
                    label = "Keamanan & Privasi",
                    iconTint = SafeGreen,
                    onClick = onSecurityClick
                )
                ProfileItem(
                    icon = Icons.Default.Notifications,
                    label = "Notifikasi",
                    iconTint = WarningYellow,
                    onClick = onNotificationClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileSection(title = "PREFERENSI") {
                ProfileToggleItem(
                    icon = Icons.Default.DarkMode,
                    label = "Mode Gelap",
                    iconTint = DeepNavy,
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle
                )
                ProfileToggleItem(
                    icon = Icons.Default.Fingerprint,
                    label = "Login Biometrik",
                    iconTint = SafeGreen,
                    checked = isBiometricEnabled,
                    onCheckedChange = onBiometricToggle
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileSection(title = "DUKUNGAN") {
                ProfileItem(
                    icon = Icons.Default.Info,
                    label = "Tentang ScamShield AI",
                    iconTint = YaleBlue,
                    onClick = onAboutClick
                )
                ProfileItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    label = "Pusat Bantuan",
                    iconTint = Color(0xFFA855F7)
                )
                ProfileItem(
                    icon = Icons.Default.Star,
                    label = "Beri Nilai Aplikasi",
                    iconTint = WarningYellow
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { onLogout() },
                color = DangerRed.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DangerRed.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = DangerRed, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Keluar Akun", color = DangerRed, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(24.dp), spotColor = YaleBlue.copy(alpha = 0.1f)),
        color = Color.White,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Cerulean.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Cerulean, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = value, color = PrussianBlue, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = label, color = Slate500, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            color = Slate500,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = YaleBlue.copy(alpha = 0.05f)),
            color = CardWhite,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.05f))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun ProfileItem(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    value: String? = null,
    badgeCount: Int = 0,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = PrussianBlue, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        
        if (value != null) {
            Text(text = value, color = Cerulean, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        } else if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(DangerRed),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Slate500, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun ProfileToggleItem(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = PrussianBlue, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    ProfileScreen(onSecurityClick = {}, onNotificationClick = {}, onAboutClick = {})
}
