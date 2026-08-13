package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.theme.*

@Composable
fun HomeScreen(
    threatCount: Int,
    onScanModeSelected: (String) -> Unit,
    onEducationSelected: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(WhiteBackground, Slate100.copy(alpha = 0.55f), WhiteBackground)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                HeroSection(threatCount, onHistoryClick)
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "Mulai periksa",
                    color = PrussianBlue,
                    fontFamily = DisplayFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pilih jenis konten yang ingin dicek.",
                    color = Slate500,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ScanModeGrid(onScanModeSelected)
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
                EducationBanner(onEducationSelected)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun HeroSection(
    threatCount: Int,
    onHistoryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(DeepNavy, YaleBlue))
            )
            .statusBarsPadding()
            .padding(bottom = 28.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ScamShield",
                    color = Color.White,
                    fontFamily = DisplayFontFamily,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable { onHistoryClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Riwayat",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    if (threatCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(DangerRed)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cek chat, tautan, screenshot, atau QR sebelum Anda bertindak.",
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            if (threatCount > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$threatCount risiko tinggi di riwayat",
                    color = Cerulean,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ScanModeGrid(onScanModeSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ScanModeItem(
                title = "Chat",
                desc = "Teks pesan",
                icon = Icons.Outlined.ChatBubbleOutline,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("chat") }
            )
            ScanModeItem(
                title = "Screenshot",
                desc = "Gambar chat",
                icon = Icons.Outlined.Image,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("screenshot") }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ScanModeItem(
                title = "Tautan",
                desc = "URL / link",
                icon = Icons.Outlined.Link,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("link") }
            )
            ScanModeItem(
                title = "QR",
                desc = "Kode QRIS",
                icon = Icons.Outlined.QrCodeScanner,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("qr") }
            )
        }
    }
}

@Composable
private fun ScanModeItem(
    title: String,
    desc: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .border(1.dp, DeepNavy.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(18.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Cerulean,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = title,
            color = PrussianBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = desc,
            color = Slate500,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun EducationBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(YaleBlue)
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Pusat Edukasi",
                color = Color.White,
                fontFamily = DisplayFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Artikel dan kuis tentang modus penipuan.",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.edukasi),
            contentDescription = null,
            modifier = Modifier
                .width(96.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeScreen(threatCount = 2, onScanModeSelected = {}, onEducationSelected = {}, onHistoryClick = {})
}
