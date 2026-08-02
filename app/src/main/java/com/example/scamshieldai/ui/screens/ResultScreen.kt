package com.example.scamshieldai.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ScanResult(
    val type: String, // "chat", "screenshot", "link", "qr"
    val riskScore: Int, // 0-100
    val riskLevel: RiskLevel,
    val inputSummary: String,
    val flags: List<String>,
    val explanation: String,
    val recommendation: String,
    val relatedArticle: String? = null
)

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

private val BgDeepNavy = Color(0xFF0B1628)
private val CardBg = Color(0xFF1E293B).copy(alpha = 0.4f)
private val HighRiskColor = Color(0xFFEF4444)
private val MediumRiskColor = Color(0xFFF59E0B)
private val LowRiskColor = Color(0xFF22C55E)
private val Slate400 = Color(0xFF94A3B8)
private val Slate500 = Color(0xFF64748B)

@Composable
fun ResultScreen(
    result: ScanResult,
    onBackToHome: () -> Unit,
    onHistoryClick: () -> Unit,
    onBlockDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val riskConfig = remember(result.riskLevel) {
        when (result.riskLevel) {
            RiskLevel.HIGH -> Triple(HighRiskColor, "Risiko Tinggi", "⚠️")
            RiskLevel.MEDIUM -> Triple(MediumRiskColor, "Risiko Sedang", "🟡")
            RiskLevel.LOW -> Triple(LowRiskColor, "Aman", "✅")
        }
    }

    // Label tipe untuk sub-judul
    val typeLabel = when(result.type) {
        "link" -> "Tautan"
        "chat" -> "Teks Chat"
        "screenshot" -> "Screenshot"
        "qr" -> "QR Code"
        else -> "Analisis"
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            Text("Hasil Analisis", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            IconButton(
                onClick = onHistoryClick,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(Icons.Default.History, contentDescription = "Riwayat", tint = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Risk Gauge
            RiskGauge(score = result.riskScore, color = riskConfig.first)

            Spacer(modifier = Modifier.height(24.dp))

            // Risk Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(riskConfig.first.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(riskConfig.third, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = riskConfig.second,
                        color = riskConfig.first,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "$typeLabel · Disimpan di riwayat",
                color = Slate500,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sections
            ResultSection(title = "INPUT YANG DIANALISIS") {
                Text(
                    text = result.inputSummary,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (result.flags.isNotEmpty()) {
                ResultSection(title = "INDIKATOR KECURIGAAN") {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        result.flags.forEach { flag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(riskConfig.first.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(flag, color = riskConfig.first.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            ResultSection(
                title = "Penjelasan AI",
                icon = Icons.Default.Info,
                iconColor = Slate400
            ) {
                Text(
                    text = result.explanation,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ResultSection(
                title = "Rekomendasi Tindakan",
                icon = Icons.Default.Warning,
                iconColor = riskConfig.first
            ) {
                Text(
                    text = result.recommendation,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onBlockDeleteClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HighRiskColor.copy(alpha = 0.15f))
                ) {
                    Text("🚫 Blokir & Hapus", color = HighRiskColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onReportClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Text("📢 Laporkan", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Learn More Card
            result.relatedArticle?.let { article ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4F46E5).copy(alpha = 0.15f))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF4F46E5).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📖", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pelajari lebih lanjut", color = Color(0xFF818CF8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(article, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF818CF8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF0D9488), Color(0xFF14B8A6))))
                    .clickable { onBackToHome() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Kembali ke Beranda",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun RiskGauge(score: Int, color: Color) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(score) {
        animatedProgress.animateTo(score / 100f, animationSpec = tween(1200))
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            // Background arc
            drawArc(
                color = Color.White.copy(alpha = 0.05f),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Progress arc
            drawArc(
                color = color,
                startAngle = 140f,
                sweepAngle = 260f * animatedProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = (animatedProgress.value * 100).toInt().toString(),
                color = Color.White,
                fontSize = 56.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "/ 100",
                color = Slate500,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ResultSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title.uppercase(),
                    color = Slate400,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
private fun ResultHighPreview() {
    ResultScreen(
        result = ScanResult(
            type = "chat",
            riskScore = 88,
            riskLevel = RiskLevel.HIGH,
            inputSummary = "Selamat! Anda terpilih mendapatkan hadiah Rp 50.000.000 dari BRI. Klik link ini ...",
            flags = listOf("Janji hadiah mencurigakan", "Tekanan waktu/urgensi", "Tautan tidak resmi", "Permintaan tindakan segera"),
            explanation = "Pesan ini mengandung beberapa tanda penipuan klasik: janji hadiah uang dalam jumlah besar, tekanan waktu (urgensi), dan tautan mencurigakan yang meniru domain resmi.",
            recommendation = "Jangan klik tautan apapun. Blokir nomor/akun pengirim. Laporkan ke platform terkait.",
            relatedArticle = "Waspada Phishing: Kenali Tautan Palsu"
        ),
        onBackToHome = {},
        onHistoryClick = {},
        onBlockDeleteClick = {},
        onReportClick = {}
    )
}
