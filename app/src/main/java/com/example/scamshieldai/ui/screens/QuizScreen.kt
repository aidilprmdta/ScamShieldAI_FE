package com.example.scamshieldai.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*

data class QuizScenario(
    val id: String,
    val scenarioNumber: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndices: List<Int>,
    val explanation: String
)

@Composable
fun QuizScreen(
    onBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scenarios = remember {
        listOf(
            QuizScenario(
                "1", 1,
                "Anda menerima SMS: \"Selamat! Rekening Anda menang Rp 10 juta. Klik http://bca-promo.xyz untuk klaim.\" Apa yang Anda lakukan?",
                listOf(
                    "Langsung klik link karena menarik",
                    "Scan dulu dengan ScamShield AI sebelum klik",
                    "Forward ke teman untuk konfirmasi",
                    "Hubungi BCA melalui 1500888 untuk verifikasi"
                ),
                listOf(1, 3),
                "Benar! Domain .xyz bukan domain resmi BCA. Selalu verifikasi melalui saluran resmi atau gunakan ScamShield AI sebelum klik tautan apapun."
            ),
            QuizScenario(
                "2", 2,
                "WhatsApp dari nomor asing: \"Kak ini aku, ganti nomor. HP rusak. Minta tolong transfer Rp 1,5 juta dulu ya urgent.\" Respons terbaik?",
                listOf(
                    "Langsung transfer karena kasihan",
                    "Tanya nama lengkap dan cerita spesifik yang hanya keluarga tahu",
                    "Hubungi nomor lama orang tersebut untuk konfirmasi",
                    "Abaikan dan blokir"
                ),
                listOf(1, 2),
                "Tepat! Penipu tidak akan bisa menjawab pertanyaan personal. Konfirmasi via nomor lama adalah cara paling aman."
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedIndices by remember { mutableStateOf(setOf<Int>()) }
    var isConfirmed by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }
    
    val currentScenario = scenarios[currentIndex]
    val scrollState = rememberScrollState()

    if (isFinished) {
        QuizResultContent(
            score = score,
            total = scenarios.size,
            onBackToEducation = onFinish
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        ScreenTopBar(
            title = "Kuis",
            subtitle = "Pertanyaan ${currentIndex + 1} dari ${scenarios.size}",
            onBack = onBack
        )

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / scenarios.size },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                .height(6.dp)
                .clip(CircleShape),
            color = Cerulean,
            trackColor = DeepNavy.copy(alpha = 0.05f)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SKENARIO ${currentScenario.scenarioNumber}",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = currentScenario.question,
                color = PrussianBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Options
            currentScenario.options.forEachIndexed { index, option ->
                val isSelected = selectedIndices.contains(index)
                val isCorrect = currentScenario.correctOptionIndices.contains(index)
                
                QuizOptionCard(
                    text = option,
                    isSelected = isSelected,
                    isCorrect = if (isConfirmed) isCorrect else null,
                    isWrong = if (isConfirmed && isSelected && !isCorrect) true else null,
                    onClick = {
                        if (!isConfirmed) {
                            selectedIndices = if (isSelected) {
                                selectedIndices - index
                            } else {
                                selectedIndices + index
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Feedback Box
            AnimatedVisibility(
                visible = isConfirmed,
                enter = fadeIn() + expandVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Cerulean.copy(alpha = 0.05f))
                        .border(1.dp, Cerulean.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = currentScenario.explanation,
                        color = YaleBlue,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }

        // Bottom Button
        Box(modifier = Modifier.padding(20.dp).navigationBarsPadding()) {
            val buttonText = if (!isConfirmed) "Konfirmasi Jawaban" 
                            else if (currentIndex < scenarios.size - 1) "Pertanyaan Berikutnya \u2192"
                            else "Lihat Hasil"
            
            val isButtonEnabled = selectedIndices.isNotEmpty()
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isButtonEnabled) Brush.linearGradient(listOf(Cerulean, YaleBlue))
                        else Brush.linearGradient(listOf(DeepNavy.copy(alpha = 0.05f), DeepNavy.copy(alpha = 0.05f)))
                    )
                    .clickable(enabled = isButtonEnabled) {
                        if (!isConfirmed) {
                            val isCorrect = currentScenario.correctOptionIndices.any { selectedIndices.contains(it) }
                            if (isCorrect) score++
                            isConfirmed = true
                        } else {
                            if (currentIndex < scenarios.size - 1) {
                                currentIndex++
                                selectedIndices = setOf()
                                isConfirmed = false
                            } else {
                                isFinished = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    color = if (isButtonEnabled) Color.White else PrussianBlue.copy(alpha = 0.2f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuizResultContent(
    score: Int,
    total: Int,
    onBackToEducation: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Celebration Icon with Pulse
        Box(
            modifier = Modifier
                .size(140.dp)
                .drawBehind {
                    drawCircle(
                        color = Cerulean.copy(alpha = 0.1f),
                        radius = (size.minDimension / 2) * scale
                    )
                }
                .border(2.dp, Cerulean.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Cerulean,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Kuis Selesai!",
            color = PrussianBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Skor Anda:",
            color = Slate500,
            fontSize = 16.sp
        )

        Text(
            text = "$score / $total",
            color = Cerulean,
            fontSize = 64.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (score == total) "Semua jawaban benar."
                   else "Beberapa jawaban masih keliru. Coba ulang bila perlu.",
            color = PrussianBlue.copy(alpha = 0.7f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Back Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Cerulean, YaleBlue)))
                .clickable { onBackToEducation() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Kembali ke Edukasi",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuizOptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    isWrong: Boolean?,
    onClick: () -> Unit
) {
    val borderColor = when {
        isCorrect == true -> SafeGreen
        isWrong == true -> DangerRed
        isSelected -> Cerulean
        else -> DeepNavy.copy(alpha = 0.1f)
    }
    
    val bgColor = when {
        isCorrect == true -> SafeGreen.copy(alpha = 0.05f)
        isWrong == true -> DangerRed.copy(alpha = 0.05f)
        else -> CardWhite
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isCorrect == true || isSelected) borderColor.copy(alpha = 0.1f) else Color.Transparent)
                    .border(1.5.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isCorrect == true || isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = borderColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = text,
                color = if (isCorrect == true || isWrong == true || isSelected) PrussianBlue else PrussianBlue.copy(alpha = 0.7f),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizPreview() {
    QuizScreen(onBack = {}, onFinish = {})
}
