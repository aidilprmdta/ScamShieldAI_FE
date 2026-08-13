package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.network.AdminReportItem
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*

@Composable
fun AdminReportsScreen(
    reports: List<AdminReportItem>,
    isLoading: Boolean = false,
    onBack: () -> Unit,
    onVerify: (String) -> Unit,
    onReject: (String) -> Unit,
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("all") }

    val stats = remember(reports) {
        mapOf(
            "pending" to reports.count { it.verifiedStatus == "pending" },
            "verified" to reports.count { it.verifiedStatus == "verified" },
            "rejected" to reports.count { it.verifiedStatus == "rejected" },
            "total" to reports.size
        )
    }

    val filteredReports = remember(selectedFilter, reports) {
        when (selectedFilter) {
            "pending" -> reports.filter { it.verifiedStatus == "pending" }
            "verified" -> reports.filter { it.verifiedStatus == "verified" }
            "rejected" -> reports.filter { it.verifiedStatus == "rejected" }
            else -> reports
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenTopBar(
            title = "Kelola laporan",
            subtitle = "Tinjau laporan pengguna",
            onBack = onBack,
            actions = {
                TextButton(onClick = onRefresh, enabled = !isLoading) {
                    Text("Muat ulang", color = Cerulean, fontSize = 13.sp)
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminFilterChip(
                label = "Semua (${stats["total"] ?: 0})",
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                modifier = Modifier.weight(1f)
            )
            AdminFilterChip(
                label = "Pending (${stats["pending"] ?: 0})",
                selected = selectedFilter == "pending",
                onClick = { selectedFilter = "pending" },
                modifier = Modifier.weight(1f)
            )
            AdminFilterChip(
                label = "OK (${stats["verified"] ?: 0})",
                selected = selectedFilter == "verified",
                onClick = { selectedFilter = "verified" },
                modifier = Modifier.weight(1f)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Cerulean)
            }
        } else if (filteredReports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(top = 80.dp), contentAlignment = Alignment.TopCenter) {
                Text("Tidak ada laporan", color = Slate500, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredReports) { report ->
                    AdminReportCard(
                        report = report,
                        onVerify = { onVerify(report.reportId) },
                        onReject = { onReject(report.reportId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Cerulean else DeepNavy.copy(alpha = 0.06f))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else PrussianBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun AdminStatCard(
    count: Int,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White.copy(alpha = 0.08f),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
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
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AdminReportCard(
    report: AdminReportItem,
    onVerify: () -> Unit,
    onReject: () -> Unit
) {
    val statusColor = when (report.verifiedStatus) {
        "verified" -> SafeGreen
        "rejected" -> DangerRed
        else -> WarningYellow
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    report.verifiedStatus.uppercase(),
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    report.type.uppercase(),
                    color = Cerulean,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                report.content,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (report.note != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Catatan: ${report.note}", color = Slate500, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Dikirim: ${report.createdAt}", color = Slate400, fontSize = 11.sp)

            if (report.verifiedStatus != "pending") {
                Spacer(modifier = Modifier.height(6.dp))
                val actor = report.verifiedByEmail ?: report.verifiedBy ?: "admin"
                val whenText = report.verifiedAt ?: "-"
                Text(
                    "Ditinjau oleh $actor • $whenText",
                    color = Slate500,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if (report.verifiedStatus == "pending") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onVerify,
                        colors = ButtonDefaults.buttonColors(containerColor = SafeGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Verifikasi", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tolak", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
