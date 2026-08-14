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
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(YaleBlue)
                .statusBarsPadding()
                .padding(bottom = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // App Logo/Icon
                Surface(
                    modifier = Modifier.size(100.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.logoapp_removebg),
                            contentDescription = "Logo",
                            modifier = Modifier.size(72.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "ScamShield AI",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Versi 2.0.0 (Stable)",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            Text(
                text = "MISI KAMI",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "ScamShield AI hadir untuk memberikan perlindungan cerdas bagi setiap pengguna digital di Indonesia. Dengan memanfaatkan teknologi kecerdasan buatan (AI) tercanggih, kami berkomitmen untuk memberantas ancaman penipuan siber, mulai dari phishing, malware, hingga rekayasa sosial secara real-time.",
                color = PrussianBlue.copy(alpha = 0.8f),
                fontSize = 15.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "FITUR UNGGULAN",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            AboutFeatureItem(
                icon = Icons.Outlined.Shield,
                title = "Analisis AI 24/7",
                desc = "Mendeteksi pola penipuan bahasa Indonesia yang kompleks secara otomatis."
            )
            AboutFeatureItem(
                icon = Icons.Default.Language,
                title = "Verifikasi Tautan Global",
                desc = "Terhubung dengan database ancaman global untuk memvalidasi setiap URL."
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Links Section
            // Section removed as requested


            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "© 2026 ScamShield AI Team.\nDibuat dengan ❤️ untuk Indonesia yang lebih aman.",
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
