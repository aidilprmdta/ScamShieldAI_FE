package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.settings.AppPreferences
import com.example.scamshieldai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SecurityPrivacyScreen(
    onBack: () -> Unit,
    onManagePermissions: () -> Unit = {},
    onChangePassword: (() -> Unit)? = null,
    onAutoCleanEnabled: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val autoCleanEnabled by AppPreferences.autoCleanEnabledFlow(context)
        .collectAsState(initial = false)

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
            Text(
                text = "Keamanan & Privasi",
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

            SecuritySection(title = "Akun") {
                SecurityNavItem(
                    icon = Icons.Default.Password,
                    label = "Ubah Kata Sandi",
                    description = "Ganti kata sandi akun email Anda",
                    iconTint = WarningYellow,
                    onClick = onChangePassword
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SecuritySection(title = "Data & izin") {
                SecurityNavItem(
                    icon = Icons.Default.Security,
                    label = "Manajemen Izin",
                    description = "Kamera, notifikasi, dan akses foto",
                    iconTint = Color(0xFFA855F7),
                    onClick = onManagePermissions
                )
                SecurityToggleItem(
                    icon = Icons.Default.DeleteSweep,
                    label = "Pembersihan Otomatis",
                    description = "Hapus riwayat deteksi setelah ${AppPreferences.AUTO_CLEAN_DAYS} hari",
                    checked = autoCleanEnabled,
                    onCheckedChange = { enabled ->
                        scope.launch {
                            AppPreferences.setAutoCleanEnabled(context, enabled)
                            if (enabled) onAutoCleanEnabled?.invoke()
                        }
                    },
                    iconTint = DangerRed
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Data dikirim lewat koneksi aman. Jangan masukkan password atau OTP di kolom scan.",
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
private fun SecuritySection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            color = Slate500,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardWhite,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.05f))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SecurityToggleItem(
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
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
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

@Composable
private fun SecurityNavItem(
    icon: ImageVector,
    label: String,
    description: String,
    iconTint: Color,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
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

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = PrussianBlue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = description, color = Slate500, fontSize = 12.sp)
        }

        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Slate500,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SecurityPrivacyPreview() {
    SecurityPrivacyScreen(onBack = {}, onChangePassword = {})
}
