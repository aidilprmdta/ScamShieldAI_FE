package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDeepNavy = Color(0xFF0B1628)
private val CardBg = Color(0xFF1E293B).copy(alpha = 0.4f)
private val InfoBg = Color(0xFF1E3A8A).copy(alpha = 0.3f)
private val InfoText = Color(0xFF93C5FD)
private val TealAccent = Color(0xFF2DD4BF)
private val Slate400 = Color(0xFF94A3B8)

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
            .background(BgDeepNavy)
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
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Scan Chat",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tempel teks pesan mencurigakan",
                    color = Slate400,
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            // Info Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(InfoBg)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = InfoText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Salin seluruh isi pesan chat, SMS, atau email mencurigakan, lalu tempel di bawah ini. Semakin lengkap teks, semakin akurat analisis AI.",
                    color = InfoText,
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
                    .clip(RoundedCornerShape(20.dp)),
                placeholder = {
                    Text(
                        "Tempel teks pesan di sini...",
                        color = Slate400,
                        fontSize = 16.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardBg,
                    unfocusedContainerColor = CardBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = TealAccent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
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
                    color = Slate400,
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
                        tint = TealAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tempel dari clipboard",
                        color = TealAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sample Modes Section
            Text(
                text = "COBA CONTOH MODUS PENIPUAN",
                color = Slate400,
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
                        .background(CardBg)
                        .clickable { inputText = example }
                        .padding(16.dp)
                ) {
                    Text(
                        text = example,
                        color = Color.White.copy(alpha = 0.8f),
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
            Button(
                onClick = { if (inputText.isNotBlank()) onAnalyze(inputText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E293B),
                    disabledContainerColor = Color(0xFF1E293B).copy(alpha = 0.5f)
                ),
                enabled = inputText.isNotBlank()
            ) {
                Text(
                    text = "Analisis Sekarang",
                    color = if (inputText.isNotBlank()) Color.White else Color.White.copy(alpha = 0.3f),
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
