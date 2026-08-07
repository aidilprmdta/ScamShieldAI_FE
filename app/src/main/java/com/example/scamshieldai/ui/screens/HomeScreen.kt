package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
            .background(WhiteBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                HeroSection(threatCount, onHistoryClick)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                ScanModeGrid(onScanModeSelected)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
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
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(Brush.verticalGradient(listOf(DeepNavy, YaleBlue)))
            .statusBarsPadding()
            .padding(bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header: Greeting and Notifications
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello,",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Pengguna",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Notification Icon
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable { onHistoryClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        if (threatCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(DangerRed)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Protection Card inside Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Cerulean.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = Cerulean,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "2026.08.02 20:58", // Real-time simulation
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Aplikasi Terlindungi Aktif",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$threatCount ancaman diblokir hari ini",
                            color = Cerulean,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanModeGrid(onScanModeSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ScanCardModern(
                title = "Scan Chat",
                desc = "Analisis teks pesan",
                icon = Icons.Outlined.ChatBubbleOutline,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("chat") }
            )
            ScanCardModern(
                title = "Screenshot",
                desc = "Ekstrak teks gambar",
                icon = Icons.Outlined.Image,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("screenshot") }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ScanCardModern(
                title = "Cek Tautan",
                desc = "Verifikasi URL aman",
                icon = Icons.Outlined.Link,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("link") }
            )
            ScanCardModern(
                title = "Scan QR",
                desc = "Pindai kode QRIS",
                icon = Icons.Outlined.QrCodeScanner,
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("qr") }
            )
        }
    }
}

@Composable
private fun ScanCardModern(
    title: String,
    desc: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = YaleBlue.copy(alpha = 0.1f)
            ),
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Cerulean,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
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
}

@Composable
private fun EducationBanner(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = DeepNavy.copy(alpha = 0.2f)
            ),
        color = YaleBlue,
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Cerulean.copy(alpha = 0.4f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("EDUKASI", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Kenali Modus Penipuan Terbaru", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Artikel & simulasi interaktif \u2192", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
            
            Image(
                painter = painterResource(id = R.drawable.edukasi),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeScreen(threatCount = 2, onScanModeSelected = {}, onEducationSelected = {}, onHistoryClick = {})
}
