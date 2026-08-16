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
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*

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
        LinkSample("http://bca-verified-promo.xyz/hadiah-2026", "SCAM", DangerRed),
        LinkSample("https://tokopedia.com/promo/harbolnas", "AMAN", SafeGreen),
        LinkSample("http://bit.ly/verifikasi-bri-urgent", "SCAM", DangerRed)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        ScreenTopBar(
            title = "Cek tautan",
            subtitle = "Tempel URL sebelum diklik",
            onBack = onBack,
            isHero = true
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

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
                        .border(1.dp, Cerulean.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    placeholder = {
                        Text("https://contoh.com/tautan", color = Slate500.copy(alpha = 0.6f), fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Link, contentDescription = null, tint = Slate500)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = CardWhite,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Cerulean,
                        focusedTextColor = PrussianBlue,
                        unfocusedTextColor = PrussianBlue
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = { clipboardManager.getText()?.let { urlText = it.text } },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardWhite)
                        .border(1.dp, DeepNavy.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.ContentPaste, contentDescription = "Tempel", tint = PrussianBlue)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Checklist Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardWhite)
                    .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        "Yang akan kami periksa:",
                        color = PrussianBlue,
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
                                tint = Cerulean,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(point, color = Slate500, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sample Scam Links
            Text(
                "CONTOH TAUTAN SCAM",
                color = Slate500,
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
                        .background(CardWhite)
                        .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .clickable { urlText = sample.url }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(sample.statusColor))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        sample.url,
                        color = PrussianBlue.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(sample.statusColor.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(sample.status, color = sample.statusColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        val isEnabled = urlText.isNotBlank()
        Button(
            onClick = { onCheck(urlText) },
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cerulean,
                disabledContainerColor = DeepNavy.copy(alpha = 0.08f)
            )
        ) {
            Text("Periksa tautan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

private data class LinkSample(val url: String, val status: String, val statusColor: Color)

@Preview(showBackground = true)
@Composable
private fun CheckLinkPreview() {
    CheckLinkScreen(onBack = {}, onCheck = {})
}
