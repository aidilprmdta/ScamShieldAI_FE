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
import com.example.scamshieldai.ui.theme.*

@Composable
fun AdminReportsScreen(
    reports: List<AdminReportItem>,
    isLoading: Boolean = false,
    onBack: () -> Unit,
    onVerify: (String) -> Unit,
    onReject: (String) -> Unit,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(YaleBlue)
                .statusBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Kelola Laporan", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text(
                            "Total ${stats["total"] ?: 0} laporan",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        count = stats["pending"] ?: 0,
                        label = "Pending",
                        color = WarningYellow,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedFilter = "pending" }
                    )
                    AdminStatCard(
                        count = stats["verified"] ?: 0,
                        label = "Verified",
                        color = SafeGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedFilter = "verified" }
                    )
                    AdminStatCard(
                        count = stats["rejected"] ?: 0,
                        label = "Rejected",
                        color = DangerRed,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedFilter = "rejected" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("all" to "Semua", "pending" to "Pending", "verified" to "Verified", "rejected" to "Rejected").forEach { (key, label) ->
                        val isSelected = selectedFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Cerulean else Color.White.copy(alpha = 0.1f))
                                .clickable { selectedFilter = key }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
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
