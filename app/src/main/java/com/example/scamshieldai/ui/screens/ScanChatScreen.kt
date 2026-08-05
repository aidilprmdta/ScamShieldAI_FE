package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanChatScreen(
    onBack: () -> Unit,
    onAnalyze: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by rememberSaveable { mutableStateOf("") }
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    val examples = listOf(
        "Selamat! Anda terpilih mendapatkan hadiah Rp 50.000.000 dari BRI. Klik link ini segera sebelum kedaluwarsa:...",
        "Mama ini nomor baru. HP lama hilang. Transfer dulu Rp 2 juta ke rek BCA 1234567890 ya ma urgent",
        "Halo, saya dari Shopee. Akun Anda terdeteksi aktivitas mencurigakan. Segera verifikasi sekarang: shopee-..."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // Hero Header for Scan Chat
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(YaleBlue)
                .statusBarsPadding()
                .padding(bottom = 32.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Scan Chat",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Tempel teks pesan mencurigakan",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Info Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(YaleBlue.copy(alpha = 0.05f))
                    .border(1.dp, YaleBlue.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = YaleBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Salin seluruh isi pesan chat, SMS, atau email mencurigakan, lalu tempel di bawah ini. Semakin lengkap teks, semakin akurat analisis AI.",
                    color = YaleBlue,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Text Input
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, YaleBlue.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
                placeholder = {
                    Text(
                        "Tempel teks pesan di sini...",
                        color = Slate500.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Cerulean,
                    focusedTextColor = PrussianBlue,
                    unfocusedTextColor = PrussianBlue
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tools (Character Count & Paste)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${inputText.length} karakter",
                    color = Slate500,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier.clickable {
                        clipboardManager.getText()?.let { inputText = it.text }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = null,
                        tint = Cerulean,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tempel dari clipboard",
                        color = Cerulean,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sample Modes Section
            Text(
                text = "COBA CONTOH MODUS PENIPUAN",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            examples.forEach { example ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .clickable { inputText = example }
                        .padding(16.dp)
                ) {
                    Text(
                        text = example,
                        color = PrussianBlue.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        maxLines = 2
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Button
        Box(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            val isEnabled = inputText.isNotBlank()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isEnabled) Brush.linearGradient(listOf(Cerulean, YaleBlue))
                        else Brush.linearGradient(listOf(DeepNavy.copy(alpha = 0.05f), DeepNavy.copy(alpha = 0.05f)))
                    )
                    .clickable(enabled = isEnabled) { onAnalyze(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Analisis Sekarang",
                    color = if (isEnabled) Color.White else PrussianBlue.copy(alpha = 0.2f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScanChatPreview() {
    ScanChatScreen(onBack = {}, onAnalyze = {})
}
