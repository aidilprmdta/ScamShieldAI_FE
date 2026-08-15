package com.example.scamshieldai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

/**
 * Top bar terang untuk layar tool / sekunder — bisa diatur menjadi hero navy rounded.
 */
@Composable
fun ScreenTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    subtitle: String? = null,
    isHero: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isHero) DeepNavy else WhiteBackground
    val contentColor = if (isHero) Color.White else PrussianBlue
    val subtitleColor = if (isHero) Color.White.copy(alpha = 0.7f) else Slate500
    val shape = if (isHero) RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp) else RectangleShape

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape)
            .statusBarsPadding()
            .padding(bottom = if (isHero) 24.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = if (isHero) 16.dp else 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = contentColor
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = contentColor,
                    fontFamily = DisplayFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = subtitleColor,
                        fontFamily = BodyFontFamily,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            actions()
        }
        if (!isHero) {
            HorizontalDivider(thickness = 1.dp, color = Color(0x14001F54))
        }
    }
}

@Composable
fun ScreenSectionTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = PrussianBlue,
            fontFamily = DisplayFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, color = SoftSlate, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

private val SoftSlate = Slate500
