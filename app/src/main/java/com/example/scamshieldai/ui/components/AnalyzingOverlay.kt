package com.example.scamshieldai.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.scamshieldai.ui.theme.*

@Composable
fun AnalyzingOverlay(
    onAnalysisComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Security UX: Block back gesture
    BackHandler { /* Do nothing - process is crucial */ }

    // Simulation logic
    LaunchedEffect(Unit) {
        delay(4000)
        onAnalysisComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground.copy(alpha = 0.95f))
            .semantics {
                contentDescription = "Menganalisis indikator penipuan. Harap tunggu."
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Animated Icon Section
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Pulsing Rings
                PulsingRing(delayMillis = 0)
                PulsingRing(delayMillis = 300)

                // Central Circle with Spinning Icon
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Cerulean.copy(alpha = 0.1f))
                        .semantics { contentDescription = "AI Analyzing Icon" },
                    contentAlignment = Alignment.Center
                ) {
                    SpinningIcon()
                }
            }

            // Text Section
            Text(
                text = "Menganalisis...",
                color = PrussianBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "AI sedang memeriksa indikator penipuan.\nHarap tunggu sebentar.",
                color = Slate500,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            // Bouncing Dots
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(3) { index ->
                    BouncingDot(delayMillis = index * 200)
                }
            }
        }
    }
}

@Composable
private fun PulsingRing(delayMillis: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "PulseRing")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, delayMillis = delayMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RingProgress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = (size.minDimension / 2) * (1f + progress * 0.2f)
        val alpha = (1f - progress) * 0.4f
        drawCircle(
            color = Cerulean,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx()),
            alpha = alpha
        )
    }
}

@Composable
private fun SpinningIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "Spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    Canvas(
        modifier = Modifier
            .size(40.dp)
            .rotate(rotation)
    ) {
        // Base circle (25% opacity)
        drawCircle(
            color = Cerulean,
            radius = size.minDimension / 2,
            style = Stroke(width = 3.dp.toPx()),
            alpha = 0.25f
        )

        // Progress arc (75% opacity)
        drawArc(
            color = Cerulean,
            startAngle = -90f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
            alpha = 0.75f
        )
    }
}

@Composable
private fun BouncingDot(delayMillis: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "Bounce")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = delayMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BounceY"
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(0, dy.dp.roundToPx()) }
            .size(8.dp)
            .clip(CircleShape)
            .background(Cerulean)
    )
}

@Preview(showBackground = true)
@Composable
private fun AnalyzingOverlayPreview() {
    AnalyzingOverlay(onAnalysisComplete = {})
}
