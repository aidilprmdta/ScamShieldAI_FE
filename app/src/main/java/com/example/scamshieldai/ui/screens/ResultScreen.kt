package com.example.scamshieldai.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
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
import com.example.scamshieldai.ui.theme.*

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ResultScreen(
    result: ScanResult,
    onBackToHome: () -> Unit,
    onHistoryClick: () -> Unit,
    onBlockDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onLearnMoreClick: (() -> Unit)? = null,
    learnMoreTitle: String? = null,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val riskConfig = remember(result.riskLevel) {
        when (result.riskLevel) {
            RiskLevel.HIGH -> DangerRed to "Risiko Tinggi"
            RiskLevel.MEDIUM -> WarningYellow to "Risiko Sedang"
            RiskLevel.LOW -> SafeGreen to "Aman"
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
            .background(WhiteBackground)
    ) {
        // Risk-tinted top strip — hasil jadi fokus, bukan hero navy
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(riskConfig.first.copy(alpha = 0.12f))
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToHome) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = PrussianBlue)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = typeLabel,
                        color = Slate500,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = riskConfig.second,
                        color = riskConfig.first,
                        fontFamily = DisplayFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onHistoryClick) {
                    Icon(Icons.Default.History, contentDescription = "Riwayat", tint = PrussianBlue)
                }
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
            
            // Risk Gauge — key agar animasi selalu reset saat analisis baru
            key(result.inputSummary, result.riskScore, result.type) {
                RiskGauge(score = result.riskScore, color = riskConfig.first)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Skor ${result.riskScore}",
                color = Slate500,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Sections
            ResultSection(title = "INPUT YANG DIANALISIS") {
                Text(
                    text = result.inputSummary,
                    color = PrussianBlue.copy(alpha = 0.8f),
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
                iconColor = Cerulean
            ) {
                Text(
                    text = result.explanation,
                    color = PrussianBlue.copy(alpha = 0.8f),
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
                    color = PrussianBlue.copy(alpha = 0.8f),
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
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.1f))
                ) {
                    Text("Blokir & Hapus", color = DangerRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onReportClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy.copy(alpha = 0.05f))
                ) {
                    Text("Laporkan", color = PrussianBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Learn More Card — navigasi ke artikel edukasi terkait
            val articleTitle = learnMoreTitle ?: result.relatedArticle
            if (!articleTitle.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(YaleBlue.copy(alpha = 0.05f))
                        .border(1.dp, YaleBlue.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .then(
                            if (onLearnMoreClick != null) Modifier.clickable(onClick = onLearnMoreClick)
                            else Modifier
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Cerulean.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Cerulean,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Pelajari lebih lanjut",
                                color = YaleBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                articleTitle,
                                color = PrussianBlue,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Buka artikel",
                            tint = YaleBlue
                        )
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
                    .background(Brush.linearGradient(listOf(Cerulean, YaleBlue)))
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
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(score / 100f, animationSpec = tween(900))
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            // Background arc
            drawArc(
                color = PrussianBlue.copy(alpha = 0.05f),
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
                color = PrussianBlue,
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
            .background(CardWhite)
            .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
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
                    color = Slate500,
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
            relatedArticle = "Phishing"
        ),
        onBackToHome = {},
        onHistoryClick = {},
        onBlockDeleteClick = {},
        onReportClick = {},
        learnMoreTitle = "Waspada Phishing: Kenali Tautan Palsu",
        onLearnMoreClick = {}
    )
}
