package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDeepNavy = Color(0xFF0B1628)
private val CardBg = Color(0xFF1E293B).copy(alpha = 0.4f)
private val HighRiskColor = Color(0xFFEF4444)
private val MediumRiskColor = Color(0xFFF59E0B)
private val LowRiskColor = Color(0xFF22C55E)
private val Slate400 = Color(0xFF94A3B8)
private val Slate500 = Color(0xFF64748B)

data class HistoryItem(
    val id: String,
    val result: ScanResult,
    val timeAgo: String
)

@Composable
fun HistoryScreen(
    historyList: List<HistoryItem>,
    onBack: () -> Unit,
    onItemClick: (HistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    
    val filteredList = remember(selectedFilter, historyList) {
        when (selectedFilter) {
            "Risiko Tinggi" -> historyList.filter { it.result.riskLevel == RiskLevel.HIGH }
            "Risiko Sedang" -> historyList.filter { it.result.riskLevel == RiskLevel.MEDIUM }
            "Aman" -> historyList.filter { it.result.riskLevel == RiskLevel.LOW }
            else -> historyList
        }
    }

    val stats = remember(historyList) {
        mapOf(
            RiskLevel.HIGH to historyList.count { it.result.riskLevel == RiskLevel.HIGH },
            RiskLevel.MEDIUM to historyList.count { it.result.riskLevel == RiskLevel.MEDIUM },
            RiskLevel.LOW to historyList.count { it.result.riskLevel == RiskLevel.LOW }
        )
    }

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
                    text = "Riwayat Deteksi",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${historyList.size} total pemindaian",
                    color = Slate400,
                    fontSize = 14.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Stats Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        count = stats[RiskLevel.HIGH] ?: 0,
                        label = "Tinggi",
                        color = HighRiskColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = stats[RiskLevel.MEDIUM] ?: 0,
                        label = "Sedang",
                        color = MediumRiskColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = stats[RiskLevel.LOW] ?: 0,
                        label = "Aman",
                        color = LowRiskColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Filter Chips
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("Semua", "Risiko Tinggi", "Risiko Sedang", "Aman")
                    items(filters) { filter ->
                        FilterChip(
                            label = filter,
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }
            }

            // History Items
            items(filteredList) { item ->
                HistoryCard(
                    item = item,
                    onClick = { onItemClick(item) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count.toString(),
                color = color,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Slate400,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color(0xFF14B8A6) else Color.White.copy(alpha = 0.1f)
    val textColor = if (isSelected) Color.White else Slate400

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HistoryCard(
    item: HistoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val result = item.result
    val riskColor = when (result.riskLevel) {
        RiskLevel.HIGH -> HighRiskColor
        RiskLevel.MEDIUM -> MediumRiskColor
        RiskLevel.LOW -> LowRiskColor
    }
    
    val riskLabel = when (result.riskLevel) {
        RiskLevel.HIGH -> "TINGGI"
        RiskLevel.MEDIUM -> "SEDANG"
        RiskLevel.LOW -> "AMAN"
    }

    val icon: ImageVector = when (result.type) {
        "chat" -> Icons.Outlined.ChatBubbleOutline
        "link" -> Icons.Outlined.Link
        "screenshot" -> Icons.Outlined.Image
        "qr" -> Icons.Outlined.QrCodeScanner
        else -> Icons.Outlined.Link
    }

    val iconBg = when (result.type) {
        "chat" -> Color(0xFF3B82F6)
        "link" -> Color(0xFF10B981)
        "screenshot" -> Color(0xFFA855F7)
        "qr" -> Color(0xFFF59E0B)
        else -> Color(0xFF3B82F6)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconBg, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(riskColor))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = riskLabel,
                        color = riskColor.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
                
                Text(
                    text = result.inputSummary,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                
                Text(
                    text = item.timeAgo,
                    color = Slate500,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = result.riskScore.toString(),
                    color = riskColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "SKOR",
                    color = Slate500,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryPreview() {
    val dummyItems = listOf(
        HistoryItem("1", ScanResult("link", 94, RiskLevel.HIGH, "http://bca-verified-promo.xyz/hadiah", emptyList(), "", ""), "1 jam lalu"),
        HistoryItem("2", ScanResult("chat", 88, RiskLevel.HIGH, "Selamat! Anda terpilih mendapatkan ha...", emptyList(), "", ""), "1 hari lalu"),
        HistoryItem("3", ScanResult("qr", 22, RiskLevel.LOW, "QR Code — tokopedia.com/promo/specia...", emptyList(), "", ""), "1 hari lalu"),
        HistoryItem("4", ScanResult("screenshot", 76, RiskLevel.MEDIUM, "Screenshot: \"Mama, ini nomor baru. Tran...", emptyList(), "", ""), "3 hari lalu")
    )
    HistoryScreen(historyList = dummyItems, onBack = {}, onItemClick = {})
}
