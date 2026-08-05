package com.example.scamshieldai.ui.screens

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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("") }
    var otherCategoryDetail by remember { mutableStateOf("") }
    var reportDetail by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    
    val categories = listOf(
        "Phishing / Tautan Palsu",
        "Penipuan Hadiah / Uang",
        "Pencurian Akun",
        "Modus Rekayasa Sosial",
        "Lainnya"
    )

    if (isSubmitted) {
        ReportSuccessContent(onBackToHome = onSubmit)
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // Hero Header for Report
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(YaleBlue)
                .statusBarsPadding()
                .padding(bottom = 32.dp)
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
                        Text(
                            text = "Laporkan Ancaman",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Bantu komunitas terhindar dari scam",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Bantu kami melindungi komunitas dengan melaporkan temuan penipuan ini.",
                color = Slate500,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "PILIH KATEGORI",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            categories.forEach { category ->
                val isSelected = selectedCategory == category
                val isOther = category == "Lainnya"
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Cerulean.copy(alpha = 0.05f) else CardWhite)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Cerulean else YaleBlue.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedCategory = category },
                                colors = RadioButtonDefaults.colors(selectedColor = Cerulean)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = category, color = PrussianBlue, fontSize = 15.sp)
                        }
                        
                        if (isOther && isSelected) {
                            Spacer(modifier = Modifier.height(12.dp))
                            TextField(
                                value = otherCategoryDetail,
                                onValueChange = { otherCategoryDetail = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                placeholder = { Text("Sebutkan kategori lainnya...", fontSize = 14.sp, color = Slate500.copy(alpha = 0.6f)) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Cerulean,
                                    unfocusedIndicatorColor = Slate500.copy(alpha = 0.2f),
                                    focusedTextColor = PrussianBlue,
                                    unfocusedTextColor = PrussianBlue
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "DETAIL TAMBAHAN (OPSIONAL)",
                color = Slate500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = reportDetail,
                onValueChange = { reportDetail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                placeholder = { Text("Ceritakan singkat kronologinya...", color = Slate500.copy(alpha = 0.6f)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = PrussianBlue,
                    unfocusedTextColor = PrussianBlue
                )
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }

        // Action Button
        Box(modifier = Modifier.padding(20.dp).navigationBarsPadding()) {
            val isEnabled = if (selectedCategory == "Lainnya") {
                selectedCategory.isNotEmpty() && otherCategoryDetail.isNotBlank()
            } else {
                selectedCategory.isNotEmpty()
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isEnabled) Brush.linearGradient(listOf(Cerulean, YaleBlue))
                        else Brush.linearGradient(listOf(DeepNavy.copy(alpha = 0.05f), DeepNavy.copy(alpha = 0.05f)))
                    )
                    .clickable(enabled = isEnabled) { isSubmitted = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Kirim Laporan",
                    color = if (isEnabled) Color.White else PrussianBlue.copy(alpha = 0.2f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ReportSuccessContent(onBackToHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Cerulean.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Cerulean,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Laporan Terkirim",
            color = PrussianBlue,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Terima kasih! Kontribusi Anda membantu AI kami menjadi lebih pintar dalam mendeteksi ancaman serupa di masa depan.",
            color = Slate500,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onBackToHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy.copy(alpha = 0.05f))
        ) {
            Text("Kembali ke Beranda", color = PrussianBlue, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportPreview() {
    ReportScreen(onBack = {}, onSubmit = {})
}
