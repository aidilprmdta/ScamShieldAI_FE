package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*
import androidx.compose.ui.res.painterResource

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        ScreenTopBar(title = "Tentang", onBack = onBack)


        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoapp_removebg),
                    contentDescription = "Logo",
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "ScamShield",
                        color = PrussianBlue,
                        fontFamily = DisplayFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Versi 1.0.0", color = Slate500, fontSize = 13.sp)
                }
            }

            Text(
                text = "TENTANG",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "ScamShield membantu memeriksa chat, tautan, screenshot, dan QR yang mencurigakan sebelum Anda klik atau transfer.",
                color = PrussianBlue.copy(alpha = 0.8f),
                fontSize = 15.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "YANG BISA DILAKUKAN",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            AboutFeatureItem(
                icon = Icons.Outlined.Shield,
                title = "Analisis teks & tautan",
                desc = "Menilai risiko pesan atau URL berdasarkan indikator penipuan umum."
            )
            AboutFeatureItem(
                icon = Icons.Default.Language,
                title = "Riwayat & edukasi",
                desc = "Menyimpan hasil analisis dan materi singkat soal modus penipuan."
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Links Section
            // Section removed as requested


            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "© 2026 ScamShield",
                color = Slate400,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun AboutFeatureItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Cerulean.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Cerulean, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, color = PrussianBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = Slate500, fontSize = 13.sp, lineHeight = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutPreview() {
    AboutScreen(onBack = {})
}
