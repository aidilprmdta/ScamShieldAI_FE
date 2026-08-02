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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
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
private val TealBg = Color(0xFF0D9488).copy(alpha = 0.1f)
private val TealAccent = Color(0xFF2DD4BF)
private val Slate400 = Color(0xFF94A3B8)
private val RedBadge = Color(0xFFEF4444)
private val GreenBadge = Color(0xFF22C55E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckLinkScreen(
    onBack: () -> Unit,
    onCheck: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var urlText by rememberSaveable { mutableStateOf("") }
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    val checkPoints = listOf(
        "Reputasi domain (Google Safe Browsing)",
        "Pola URL phishing & typosquatting",
        "Validasi HTTPS & sertifikat",
        "Ekspansi shortlink mencurigakan"
    )

    val sampleLinks = listOf(
        LinkSample("http://bca-verified-promo.xyz/hadiah-2026", "SCAM", RedBadge),
        LinkSample("https://tokopedia.com/promo/harbolnas", "AMAN", GreenBadge),
        LinkSample("http://bit.ly/verifikasi-bri-urgent", "SCAM", RedBadge)
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
                    text = "Cek Tautan",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Verifikasi keamanan URL sebelum diklik",
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
            // Info Box (Teal)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TealBg)
                    .border(1.dp, TealAccent.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = TealAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tempel tautan (URL) yang Anda terima sebelum mengkliknya. AI akan mengecek reputasi domain, pola phishing, dan keamanannya.",
                    color = TealAccent,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // URL Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, TealAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    placeholder = {
                        Text("https://contoh.com/tautan", color = Slate400, fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Link, contentDescription = null, tint = Slate400)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardBg,
                        unfocusedContainerColor = CardBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = TealAccent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = { clipboardManager.getText()?.let { urlText = it.text } },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBg)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.ContentPaste, contentDescription = "Tempel", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Checklist Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBg)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        "Yang akan kami periksa:",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    checkPoints.forEach { point ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TealAccent.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(point, color = Slate400, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sample Scam Links
            Text(
                "CONTOH TAUTAN SCAM",
                color = Slate400,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            sampleLinks.forEach { sample ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBg)
                        .clickable { urlText = sample.url }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(sample.statusColor))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        sample.url,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(sample.statusColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(sample.status, color = sample.statusColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
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
                onClick = { if (urlText.isNotBlank()) onCheck(urlText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E293B),
                    disabledContainerColor = Color(0xFF1E293B).copy(alpha = 0.5f)
                ),
                enabled = urlText.isNotBlank()
            ) {
                Text(
                    text = "Periksa Keamanan Tautan",
                    color = if (urlText.isNotBlank()) Color.White else Color.White.copy(alpha = 0.3f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private data class LinkSample(val url: String, val status: String, val statusColor: Color)

@Preview(showBackground = true)
@Composable
private fun CheckLinkPreview() {
    CheckLinkScreen(onBack = {}, onCheck = {})
}
