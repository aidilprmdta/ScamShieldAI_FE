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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.network.UserReportItem
import com.example.scamshieldai.ui.theme.*

@Composable
fun MyReportsScreen(
    reports: List<UserReportItem>,
    isLoading: Boolean = false,
    onBack: () -> Unit,
    onReportClick: (UserReportItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(YaleBlue)
                .statusBarsPadding()
                .padding(24.dp)
        ) {
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
                    Text("Laporan Saya", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Pantau status laporan yang Anda kirim", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        }

        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Cerulean)
            }
            reports.isEmpty() -> Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("Belum ada laporan", color = Slate500, fontSize = 16.sp)
            }
            else -> LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reports) { report ->
                    UserReportCard(report = report, onClick = { onReportClick(report) })
                }
            }
        }
    }
}

@Composable
fun ReportStatusScreen(
    report: UserReportItem?,
    isLoading: Boolean = false,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(YaleBlue)
                .statusBarsPadding()
                .padding(24.dp)
        ) {
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
                Text("Detail Laporan", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Cerulean)
            }
            report == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Laporan tidak ditemukan", color = Slate500)
            }
            else -> Column(Modifier.padding(24.dp)) {
                val statusColor = when (report.verifiedStatus) {
                    "verified" -> SafeGreen
                    "rejected" -> DangerRed
                    else -> WarningYellow
                }
                val statusLabel = when (report.verifiedStatus) {
                    "verified" -> "Diverifikasi"
                    "rejected" -> "Ditolak"
                    else -> "Menunggu Review"
                }

                Surface(shape = RoundedCornerShape(20.dp), color = statusColor.copy(alpha = 0.12f)) {
                    Text(
                        statusLabel.uppercase(),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = statusColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                DetailRow("Tipe", report.type.uppercase())
                DetailRow("Dikirim", report.createdAt)
                report.verifiedAt?.let { DetailRow("Diverifikasi", it) }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Isi Laporan", color = Slate500, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(report.content, color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp)
                report.note?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Catatan", color = Slate500, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun UserReportCard(report: UserReportItem, onClick: () -> Unit) {
    val statusColor = when (report.verifiedStatus) {
        "verified" -> SafeGreen
        "rejected" -> DangerRed
        else -> WarningYellow
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                Spacer(Modifier.width(8.dp))
                Text(report.verifiedStatus.uppercase(), color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(report.type.uppercase(), color = Cerulean, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text(report.content, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(report.createdAt, color = Slate400, fontSize = 11.sp)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, color = Slate500, fontSize = 13.sp, modifier = Modifier.width(100.dp))
        Text(value, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
