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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*

data class HistoryItem(
    val id: String,
    val result: ScanResult,
    val timeAgo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyList: List<HistoryItem>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onItemClick: (HistoryItem) -> Unit,
    onDeleteItem: (HistoryItem) -> Unit = {},
    onRefresh: () -> Unit = {},
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
            .background(WhiteBackground)
    ) {
        ScreenTopBar(
            title = "Riwayat",
            subtitle = "Hasil analisis sebelumnya",
            onBack = onBack
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HistoryStatText(stats[RiskLevel.HIGH] ?: 0, "tinggi", DangerRed)
            HistoryStatText(stats[RiskLevel.MEDIUM] ?: 0, "sedang", WarningYellow)
            HistoryStatText(stats[RiskLevel.LOW] ?: 0, "aman", SafeGreen)
        }

        val pullRefreshState = rememberPullToRefreshState()

        Box(modifier = Modifier.fillMaxSize()) {
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = onRefresh,
                state = pullRefreshState,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 24.dp, bottom = 110.dp)
                ) {
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

                    if (!isLoading && filteredList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Outlined.ChatBubbleOutline,
                                        contentDescription = null,
                                        tint = Slate400,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = when {
                                            errorMessage != null -> "Gagal memuat riwayat"
                                            !isLoggedIn -> "Login untuk melihat riwayat"
                                            else -> "Belum ada riwayat"
                                        },
                                        color = Slate500,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = when {
                                            errorMessage != null -> errorMessage
                                            !isLoggedIn -> "Hasil scan tersimpan setelah Anda login"
                                            else -> "Hasil deteksi akan muncul di sini setelah scan saat login"
                                        },
                                        color = Slate400,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    items(filteredList) { item ->
                        HistoryCard(
                            item = item,
                            onClick = { onItemClick(item) },
                            onDelete = { onDeleteItem(item) },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun HistoryStatText(count: Int, label: String, color: Color) {
    Column {
        Text(
            text = count.toString(),
            color = color,
            fontFamily = DisplayFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, color = Slate500, fontSize = 12.sp)
    }
}

@Composable
private fun StatCardHistory(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White.copy(alpha = 0.08f),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                color = color,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
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
    val bgColor = if (isSelected) Cerulean else DeepNavy.copy(alpha = 0.05f)
    val textColor = if (isSelected) Color.White else Slate500

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
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val result = item.result
    val riskColor = when (result.riskLevel) {
        RiskLevel.HIGH -> DangerRed
        RiskLevel.MEDIUM -> WarningYellow
        RiskLevel.LOW -> SafeGreen
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

    val iconColor = when (result.type) {
        "chat" -> Cerulean
        "link" -> SafeGreen
        "screenshot" -> Color(0xFFA855F7)
        "qr" -> WarningYellow
        else -> Cerulean
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
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
                    color = PrussianBlue,
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
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = Slate500.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onDelete() }
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
