package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.model.EducationContent
import com.example.scamshieldai.ui.theme.*

@Composable
fun EducationDetailScreen(
    content: EducationContent,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
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
                onClick = onBack,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepNavy.copy(alpha = 0.05f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = PrussianBlue)
            }
            Text("Artikel", color = PrussianBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(content.duration, color = Slate500, fontSize = 14.sp)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(DeepNavy.copy(alpha = 0.05f))
            ) {
                if (content.imageResId != null) {
                    Image(
                        painter = painterResource(id = content.imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Category Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Cerulean.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(content.category, color = Cerulean, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = content.title,
                    color = PrussianBlue,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                content.description.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        color = PrussianBlue.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Protection Tips Card
                if (content.tips.isNotEmpty()) {
                    ProtectionTipsCard(content.tips)
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun ProtectionTipsCard(tips: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Tips Perlindungan",
                    color = PrussianBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            tips.forEachIndexed { index, tip ->
                NumberedTipItem(number = index + 1, text = tip)
                if (index < tips.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun NumberedTipItem(number: Int, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Cerulean.copy(alpha = 0.1f))
                .border(1.dp, Cerulean.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = Cerulean,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = PrussianBlue.copy(alpha = 0.8f),
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EducationDetailPreview() {
    EducationDetailScreen(
        content = EducationContent(
            id = "1",
            title = "Waspada Phishing: Kenali Tautan Palsu",
            category = "Phishing",
            duration = "3 menit",
            description = listOf(
                "Phishing adalah modus penipuan digital di mana penipu membuat situs web atau pesan palsu yang menyerupai lembaga resmi (bank, e-commerce, pemerintah) untuk mencuri data pribadi dan keuangan Anda.",
                "Para penipu biasanya mengirimkan tautan melalui SMS, WhatsApp, atau email yang terlihat resmi. Tautan tersebut membawa Anda ke situs palsu yang sangat mirip dengan aslinya."
            ),
            tips = listOf(
                "Periksa URL dengan teliti — domain resmi tidak menggunakan .xyz, .info, atau angka acak",
                "Bank dan e-commerce TIDAK pernah meminta password via link",
                "Waspadai pesan dengan urgensi \"segera\" atau \"dalam 24 jam\"",
                "Gunakan ScamShield AI sebelum klik tautan apapun yang mencurigakan"
            )
        ),
        onBack = {}
    )
}
