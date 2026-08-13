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
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*

@Composable
fun ReportScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    onSubmitReport: ((String, String, String?, (Boolean, String?) -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("") }
    var otherCategoryDetail by remember { mutableStateOf("") }
    var reportDetail by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
        ScreenTopBar(
            title = "Laporkan",
            subtitle = "Kirim temuan untuk ditinjau",
            onBack = if (isSubmitting) null else onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Isi kategori dan detail temuan. Laporan masuk ke antrean peninjauan.",
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
                        .clickable(enabled = !isSubmitting) { selectedCategory = category }
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSubmitting) selectedCategory = category },
                                enabled = !isSubmitting,
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
                                enabled = !isSubmitting,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                placeholder = {
                                    Text(
                                        "Sebutkan kategori lainnya...",
                                        fontSize = 14.sp,
                                        color = Slate500.copy(alpha = 0.6f)
                                    )
                                },
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
                enabled = !isSubmitting,
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

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage!!,
                    color = DangerRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        Box(modifier = Modifier.padding(20.dp).navigationBarsPadding()) {
            val isEnabled = if (selectedCategory == "Lainnya") {
                selectedCategory.isNotEmpty() && otherCategoryDetail.isNotBlank() && !isSubmitting
            } else {
                selectedCategory.isNotEmpty() && !isSubmitting
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isEnabled) Brush.linearGradient(listOf(Cerulean, YaleBlue))
                        else Brush.linearGradient(
                            listOf(
                                DeepNavy.copy(alpha = 0.05f),
                                DeepNavy.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .clickable(enabled = isEnabled) {
                        val reportType = when (selectedCategory) {
                            "Phishing / Tautan Palsu" -> "link"
                            "Penipuan Hadiah / Uang" -> "chat"
                            "Pencurian Akun" -> "chat"
                            "Modus Rekayasa Sosial" -> "chat"
                            "Lainnya" -> "other"
                            else -> "other"
                        }
                        val content =
                            if (selectedCategory == "Lainnya") otherCategoryDetail.trim()
                            else selectedCategory
                        val note = reportDetail.trim().ifBlank { null }

                        if (onSubmitReport == null) {
                            isSubmitted = true
                            return@clickable
                        }

                        isSubmitting = true
                        errorMessage = null
                        onSubmitReport(reportType, content, note) { success, message ->
                            isSubmitting = false
                            if (success) {
                                isSubmitted = true
                            } else {
                                errorMessage = message ?: "Gagal mengirim laporan"
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
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
