package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsNone
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.network.NotificationItem
import com.example.scamshieldai.settings.AppPreferences
import com.example.scamshieldai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(
    notifications: List<NotificationItem> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onBack: () -> Unit,
    onRefresh: () -> Unit = {},
    onNotificationClick: (NotificationItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isAllNotificationsEnabled by AppPreferences.notifAllFlow(context).collectAsState(initial = true)
    val isSecurityAlertsEnabled by AppPreferences.notifSecurityFlow(context).collectAsState(initial = true)
    val isEducationUpdatesEnabled by AppPreferences.notifEducationFlow(context).collectAsState(initial = true)
    val isSystemInfoEnabled by AppPreferences.notifSystemFlow(context).collectAsState(initial = false)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .statusBarsPadding()
    ) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notifikasi",
                    color = PrussianBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inbox & pengaturan",
                    color = Slate500,
                    fontSize = 13.sp
                )
            }
            TextButton(onClick = onRefresh) {
                Text("Muat ulang", color = YaleBlue, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "INBOX",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = YaleBlue)
                    }
                }
                errorMessage != null -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DangerRed.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = DangerRed,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                notifications.isEmpty() -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = CardWhite,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.05f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.NotificationsNone,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Belum ada notifikasi", color = Slate500, fontWeight = FontWeight.SemiBold)
                            Text(
                                "Peringatan keamanan & status laporan akan muncul di sini",
                                color = Slate400,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                else -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = YaleBlue.copy(alpha = 0.05f)),
                        color = CardWhite,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.05f))
                    ) {
                        Column {
                            notifications.forEachIndexed { index, item ->
                                NotificationInboxItem(
                                    item = item,
                                    onClick = { onNotificationClick(item) }
                                )
                                if (index < notifications.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        thickness = 0.5.dp,
                                        color = Slate100
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

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
                            text = "Aktifkan semua pemberitahuan push",
                            color = if (isAllNotificationsEnabled) Color.White.copy(alpha = 0.7f) else Slate500,
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = isAllNotificationsEnabled,
                        onCheckedChange = { enabled ->
                            scope.launch { AppPreferences.setNotifAll(context, enabled) }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Cerulean,
                            checkedTrackColor = Color.White,
                            uncheckedThumbColor = Slate400,
                            uncheckedTrackColor = Slate100
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                        description = "Status laporan dan peringatan",
                        checked = isSecurityAlertsEnabled && isAllNotificationsEnabled,
                        onCheckedChange = {
                            if (isAllNotificationsEnabled) {
                                scope.launch { AppPreferences.setNotifSecurity(context, it) }
                            }
                        },
                        iconTint = DangerRed
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp, color = Slate100)
                    NotificationSettingItem(
                        icon = Icons.Outlined.School,
                        label = "Update Edukasi",
                        description = "Materi penipuan terbaru",
                        checked = isEducationUpdatesEnabled && isAllNotificationsEnabled,
                        onCheckedChange = {
                            if (isAllNotificationsEnabled) {
                                scope.launch { AppPreferences.setNotifEducation(context, it) }
                            }
                        },
                        iconTint = Cerulean
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp, color = Slate100)
                    NotificationSettingItem(
                        icon = Icons.Outlined.Info,
                        label = "Pengumuman Sistem",
                        description = "Pembaruan aplikasi & tips",
                        checked = isSystemInfoEnabled && isAllNotificationsEnabled,
                        onCheckedChange = {
                            if (isAllNotificationsEnabled) {
                                scope.launch { AppPreferences.setNotifSystem(context, it) }
                            }
                        },
                        iconTint = YaleBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun NotificationInboxItem(
    item: NotificationItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(if (item.read) Slate100 else Cerulean)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = PrussianBlue,
                fontSize = 14.sp,
                fontWeight = if (item.read) FontWeight.Medium else FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.body, color = Slate500, fontSize = 12.sp, lineHeight = 18.sp)
            if (item.createdAt.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = item.createdAt.take(19).replace('T', ' '), color = Slate400, fontSize = 11.sp)
            }
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
