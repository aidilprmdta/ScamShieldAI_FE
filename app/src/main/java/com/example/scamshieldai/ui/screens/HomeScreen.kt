package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCodeScanner

private val BgDeepNavy = Color(0xFF0B1628)
private val CardBg = Color(0xFF1E293B).copy(alpha = 0.4f)
private val TealAccent = Color(0xFF2DD4BF)
private val Slate400 = Color(0xFF94A3B8)
private val Slate500 = Color(0xFF64748B)

@Composable
fun HomeScreen(
    threatCount: Int,
    onScanModeSelected: (String) -> Unit,
    onEducationSelected: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgDeepNavy)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            HomeHeader(threatCount, onHistoryClick)
        }

        item {
            ProtectionStatusCard(threatCount)
        }

        item {
            ScanModeSection(onScanModeSelected)
        }

        item {
            RecentDetectionsSection()
        }

        item {
            EducationBanner(onEducationSelected)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HomeHeader(threatCount: Int, onHistoryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "SELAMAT PAGI",
                color = Slate400,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Tetap Aman Hari Ini",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .clickable { onHistoryClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            
            if (threatCount > 0) {
                // Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = threatCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ProtectionStatusCard(threatCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF0D9488).copy(alpha = 0.8f), Color(0xFF14B8A6).copy(alpha = 0.4f))
                )
            )
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                // Shield Icon Placeholder
                Canvas(modifier = Modifier.size(32.dp)) {
                    drawCircle(color = Color.White, alpha = 0.9f)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Status Perlindungan", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Text("Aktif & Siap", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("$threatCount ancaman terdeteksi sejauh ini", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ScanModeSection(onScanModeSelected: (String) -> Unit) {
    Column {
        Text(
            "PILIH MODE SCAN",
            color = Slate400,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ScanCard(
                title = "Scan Chat",
                desc = "Tempel teks pesan mencurigakan",
                icon = Icons.Outlined.ChatBubbleOutline,
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("chat") }
            )
            ScanCard(
                title = "Scan Screenshot",
                desc = "Upload foto tangkapan layar",
                icon = Icons.Outlined.Image,
                color = Color(0xFFA855F7),
                badge = "OCR",
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("screenshot") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ScanCard(
                title = "Cek Tautan",
                desc = "Verifikasi keamanan URL",
                icon = Icons.Outlined.Link,
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("link") }
            )
            ScanCard(
                title = "Scan QR",
                desc = "Pindai kode QR mencurigakan",
                icon = Icons.Outlined.QrCodeScanner,
                color = Color(0xFFF59E0B),
                badge = "LIVE",
                modifier = Modifier.weight(1f),
                onClick = { onScanModeSelected("qr") }
            )
        }
    }
}

@Composable
private fun ScanCard(
    title: String,
    desc: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    badge: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = Slate400, fontSize = 11.sp, lineHeight = 14.sp)
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(badge, color = Slate400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RecentDetectionsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("DETEKSI TERBARU", color = Slate400, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text("Lihat semua", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetectionItem("Phishing BCA", "1 jam lalu", Color(0xFFEF4444))
        Spacer(modifier = Modifier.height(12.dp))
        DetectionItem("Promo Palsu", "Kemarin", Color(0xFFF59E0B))
    }
}

@Composable
private fun DetectionItem(title: String, time: String, statusColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(time, color = Slate500, fontSize = 12.sp)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Slate500, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun EducationBanner(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF4F46E5).copy(alpha = 0.2f), Color(0xFF7C3AED).copy(alpha = 0.2f))))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF4F46E5).copy(alpha = 0.4f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("EDUKASI", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Kenali Modus Penipuan Terbaru", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Artikel & simulasi interaktif \u2192", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
            
            // Illustration Placeholder
            Box(modifier = Modifier.size(60.dp).background(Color.White.copy(alpha = 0.05f)))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeScreen(threatCount = 2, onScanModeSelected = {}, onEducationSelected = {}, onHistoryClick = {})
}
