package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.example.scamshieldai.R
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
    var itemToDelete by remember { mutableStateOf<HistoryItem?>(null) }
    
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavy)
    ) {
        // Decorative Circles (Hero Style)
        Canvas(modifier = Modifier.size(200.dp).offset(x = (-50).dp, y = (-50).dp)) {
            drawCircle(
                color = Color.White.copy(alpha = 0.03f),
                radius = size.minDimension / 1.1f
            )
        }

        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp, start = 16.dp, end = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Riwayat",
                        color = Color.White,
                        fontFamily = DisplayFontFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ringkasan hasil analisis Anda",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Image(
                    painter = painterResource(id = R.drawable.logoapp_removebg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        // Main Container (White Surface)
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp),
            color = WhiteBackground,
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            val pullRefreshState = rememberPullToRefreshState()

            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = onRefresh,
                state = pullRefreshState,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 110.dp)
                ) {
                    // 3 Stat Cards at the top of the container
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCardHistory(
                                count = stats[RiskLevel.HIGH] ?: 0,
                                label = "Tinggi",
                                color = DangerRed,
                                modifier = Modifier.weight(1f)
                            )
                            StatCardHistory(
                                count = stats[RiskLevel.MEDIUM] ?: 0,
                                label = "Sedang",
                                color = WarningYellow,
                                modifier = Modifier.weight(1f)
                            )
                            StatCardHistory(
                                count = stats[RiskLevel.LOW] ?: 0,
                                label = "Aman",
                                color = SafeGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Filter Buttons (Pengelompokan)
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp),
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

                    // Empty State
                    if (!isLoading && filteredList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Outlined.ChatBubbleOutline,
                                        contentDescription = null,
                                        tint = Slate100,
                                        modifier = Modifier.size(80.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = when {
                                            errorMessage != null -> "Gagal memuat"
                                            !isLoggedIn -> "Login diperlukan"
                                            else -> "Belum ada data"
                                        },
                                        color = PrussianBlue,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = when {
                                            errorMessage != null -> errorMessage
                                            !isLoggedIn -> "Masuk untuk melihat riwayat scan"
                                            else -> "Hasil analisis akan muncul di sini"
                                        },
                                        color = Slate500,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // History Cards List
                    items(filteredList) { item ->
                        HistoryCard(
                            item = item,
                            onClick = { onItemClick(item) },
                            onDelete = { itemToDelete = item },
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { itemToDelete = null },
                title = { 
                    Text(
                        "Hapus Riwayat?", 
                        fontWeight = FontWeight.Bold,
                        color = PrussianBlue
                    ) 
                },
                text = { 
                    Text(
                        "Apakah Anda yakin ingin menghapus hasil analisis ini dari riwayat Anda?",
                        color = Slate500
                    ) 
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemToDelete?.let { onDeleteItem(it) }
                            itemToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = DangerRed)
                    ) {
                        Text("Ya, Hapus", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { itemToDelete = null },
                        colors = ButtonDefaults.textButtonColors(contentColor = Slate500)
                    ) {
                        Text("Tidak")
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
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
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, DeepNavy.copy(alpha = 0.05f)),
        modifier = modifier.shadow(8.dp, RoundedCornerShape(20.dp), spotColor = color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                color = color,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = DisplayFontFamily
            )
            Text(
                text = label,
                color = Slate500,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
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
    val bgColor = if (isSelected) Cerulean else Color.White
    val textColor = if (isSelected) Color.White else Slate500
    val borderColor = if (isSelected) Cerulean else Slate100

    Surface(
        modifier = Modifier
            .clickable { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
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

    // Icon color follows risk level
    val iconColor = riskColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, DeepNavy.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconColor.copy(alpha = 0.08f)),
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
                        color = riskColor,
                        fontSize = 10.sp,
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
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = result.riskScore.toString(),
                    color = riskColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "SKOR",
                    color = Slate500.copy(alpha = 0.6f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = Slate400,
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
