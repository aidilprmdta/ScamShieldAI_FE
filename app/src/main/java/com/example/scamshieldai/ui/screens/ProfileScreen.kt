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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userName: String = "Pengguna",
    userEmail: String = "",
    scanCount: Int = 0,
    threatCount: Int = 0,
    isAdmin: Boolean = false,
    pendingMyReportsCount: Int = 0,
    pendingAdminReportsCount: Int = 0,
    onSecurityClick: () -> Unit, onAboutClick: () -> Unit,
    onHelpClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onMyReportsClick: () -> Unit = {},
    onAdminReportsClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // --- HEADER HERO CARD ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp)
                .statusBarsPadding(),
            color = DeepNavy,
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Cerulean,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = DisplayFontFamily
                )
                Text(
                    text = userEmail.ifEmpty { "Pengguna ScamShield AI" },
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderStatItem(value = threatCount.toString(), label = "Ancaman")
                    VerticalDivider(modifier = Modifier.height(24.dp), color = Color.White.copy(alpha = 0.1f))
                    HeaderStatItem(value = scanCount.toString(), label = "Total Scan")
                    VerticalDivider(modifier = Modifier.height(24.dp), color = Color.White.copy(alpha = 0.1f))
                    HeaderStatItem(value = pendingMyReportsCount.toString(), label = "Laporan")
                }
            }
        }

        // --- SCROLLABLE CONTENT ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            if (isAdmin) {
                MenuSectionTitle(title = "ADMIN PANEL", showViewAll = false)
                Spacer(modifier = Modifier.height(12.dp))
                SectionContainer {
                    ActivityItem(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "Kelola Laporan",
                        subtitle = "Tinjau temuan komunitas",
                        color = Cerulean,
                        badgeCount = pendingAdminReportsCount,
                        onClick = onAdminReportsClick
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            MenuSectionTitle(title = "PENGATURAN", showViewAll = false)
            Spacer(modifier = Modifier.height(16.dp))

            SectionContainer {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FolderMenuCard(
                        icon = Icons.Outlined.Lock,
                        title = "Keamanan",
                        modifier = Modifier.weight(1f),
                        onClick = onSecurityClick,
                    )
                    FolderMenuCard(
                        icon = Icons.Outlined.Person,
                        title = "Edit Profil",
                        modifier = Modifier.weight(1f),
                        onClick = onEditProfileClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            MenuSectionTitle(title = "AKTIVITAS & DUKUNGAN", showViewAll = false)
            Spacer(modifier = Modifier.height(12.dp))

            SectionContainer {
                Column {
                    ActivityItem(
                        icon = Icons.Outlined.Campaign,
                        title = "Laporan Saya",
                        subtitle = "Cek status laporan Anda",
                        color = Cerulean,
                        badgeCount = pendingMyReportsCount,
                        onClick = onMyReportsClick
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Slate100)
                    ActivityItem(
                        icon = Icons.Outlined.Info,
                        title = "Tentang Aplikasi",
                        subtitle = "Versi dan informasi ScamShield",
                        color = Cerulean,
                        onClick = onAboutClick
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Slate100)
                    ActivityItem(
                        icon = Icons.AutoMirrored.Filled.Help,
                        title = "Pusat Bantuan",
                        subtitle = "Bantuan dan panduan penggunaan",
                        color = Cerulean,
                        onClick = onHelpClick
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Slate100)
                    ActivityItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Keluar Akun",
                        subtitle = "Hapus sesi login perangkat ini",
                        color = Cerulean,
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { 
                    Text(
                        "Keluar Akun?", 
                        fontWeight = FontWeight.Bold,
                        color = PrussianBlue
                    ) 
                },
                text = { 
                    Text(
                        "Apakah Anda yakin ingin keluar dari akun ScamShield AI Anda?",
                        color = Slate500
                    ) 
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = DangerRed)
                    ) {
                        Text("Ya, Keluar", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = Slate500)
                    ) {
                        Text("Batal")
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
private fun SectionContainer(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(24.dp)
    ) {
        content()
    }
}

@Composable
private fun HeaderStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = DisplayFontFamily
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MenuSectionTitle(
    title: String, 
    showViewAll: Boolean, 
    viewAllLabel: String = "lihat semua",
    onViewAllClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Slate500.copy(alpha = 0.8f),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        if (showViewAll) {
            Text(
                text = viewAllLabel,
                color = Slate400,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }
    }
}

@Composable
private fun FolderMenuCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Cerulean.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Cerulean,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = PrussianBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ActivityItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    badgeCount: Int = 0,
    onClick: () -> Unit
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
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = PrussianBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = Slate500, fontSize = 13.sp)
        }
        
        if (badgeCount > 0) {
            Surface(
                color = DangerRed,
                shape = CircleShape,
                modifier = Modifier.size(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    ProfileScreen(
        onSecurityClick = {},
        onAboutClick = {}
    )
}
