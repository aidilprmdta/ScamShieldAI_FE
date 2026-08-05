package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

@Composable
fun NotificationScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    // Settings States
    var isAllNotificationsEnabled by remember { mutableStateOf(true) }
    var isSecurityAlertsEnabled by remember { mutableStateOf(true) }
    var isEducationUpdatesEnabled by remember { mutableStateOf(true) }
    var isSystemInfoEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepNavy.copy(alpha = 0.05f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = PrussianBlue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Pengaturan Notifikasi",
                color = PrussianBlue,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Main Toggle
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = YaleBlue.copy(alpha = 0.1f)),
                color = if (isAllNotificationsEnabled) YaleBlue else CardWhite,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isAllNotificationsEnabled) Color.White.copy(alpha = 0.2f) else Cerulean.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.NotificationsActive, 
                            contentDescription = null, 
                            tint = if (isAllNotificationsEnabled) Color.White else Cerulean,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Izinkan Notifikasi", 
                            color = if (isAllNotificationsEnabled) Color.White else PrussianBlue, 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Aktifkan semua pemberitahuan", 
                            color = if (isAllNotificationsEnabled) Color.White.copy(alpha = 0.7f) else Slate500, 
                            fontSize = 12.sp
                        )
                    }
                    
                    Switch(
                        checked = isAllNotificationsEnabled,
                        onCheckedChange = { isAllNotificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Cerulean,
                            checkedTrackColor = Color.White,
                            uncheckedThumbColor = Slate400,
                            uncheckedTrackColor = Slate100
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sub Settings (Enabled only if Main Toggle is ON)
            Text(
                text = "KATEGORI NOTIFIKASI",
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
                Column(modifier = Modifier.alpha(if (isAllNotificationsEnabled) 1f else 0.5f)) {
                    NotificationSettingItem(
                        icon = Icons.Outlined.ErrorOutline,
                        label = "Peringatan Keamanan",
                        description = "Deteksi link & pesan mencurigakan",
                        checked = isSecurityAlertsEnabled && isAllNotificationsEnabled,
                        onCheckedChange = { if (isAllNotificationsEnabled) isSecurityAlertsEnabled = it },
                        iconTint = DangerRed
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp, color = Slate100)
                    
                    NotificationSettingItem(
                        icon = Icons.Outlined.School,
                        label = "Update Edukasi",
                        description = "Informasi materi penipuan terbaru",
                        checked = isEducationUpdatesEnabled && isAllNotificationsEnabled,
                        onCheckedChange = { if (isAllNotificationsEnabled) isEducationUpdatesEnabled = it },
                        iconTint = Cerulean
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp, color = Slate100)
                    
                    NotificationSettingItem(
                        icon = Icons.Outlined.Info,
                        label = "Pengumuman Sistem",
                        description = "Pembaruan aplikasi & tips berkala",
                        checked = isSystemInfoEnabled && isAllNotificationsEnabled,
                        onCheckedChange = { if (isAllNotificationsEnabled) isSystemInfoEnabled = it },
                        iconTint = YaleBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Catatan: Peringatan keamanan yang sangat mendesak mungkin tetap muncul demi perlindungan akun Anda.",
                color = Slate500,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun NotificationSettingItem(
    icon: ImageVector,
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconTint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = PrussianBlue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = description, color = Slate500, fontSize = 12.sp)
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Cerulean,
                uncheckedThumbColor = Slate400,
                uncheckedTrackColor = Slate100
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationPreview() {
    NotificationScreen(onBack = {})
}
