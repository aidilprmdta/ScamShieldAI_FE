package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.theme.*

data class EducationItem(
    val id: String,
    val title: String,
    val category: String,
    val type: String, // "ARTIKEL", "KUIS", "BACAAN WAJIB"
    val duration: String,
    val imageResId: Int? = null
)

@Composable
fun EducationCenterScreen(
    completedIds: Set<String>,
    onBack: () -> Unit,
    onItemClick: (EducationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Semua") }

    val allItems = listOf(
        EducationItem("0", "Panduan Lengkap Mengenali Penipuan Digital 2026", "Keamanan", "BACAAN WAJIB", "10 menit", R.drawable.bacaan_wajib),
        EducationItem("1", "Waspada Phishing: Kenali Tautan Palsu", "Phishing", "ARTIKEL", "3 menit", R.drawable.phising),
        EducationItem("2", "Modus \"Mama Minta Transfer\" — Rekayasa Sosial", "Rekayasa Sosial", "ARTIKEL", "4 menit", R.drawable.chatpalsu),
        EducationItem("3", "QRIS Palsu & Quishing — Bahaya di Balik QR Code", "QRIS Palsu", "ARTIKEL", "3 menit", R.drawable.qushing),
        EducationItem("5", "Jangan Asal Scan! Cara Cerdas Membedakan QRIS Asli vs QRIS Palsu", "QRIS Palsu", "ARTIKEL", "5 menit", R.drawable.qushing),
        EducationItem("6", "Terlanjur Klik Link Mencurigakan? Lakukan 5 Langkah Penyelamatan Darurat!", "Keamanan", "ARTIKEL", "4 menit", R.drawable.scamp_no_palsu),
        EducationItem("7", "Mengenal 7 Wajah Phishing: Jangan Terkecoh Modus yang Mengintai Anda!", "Phishing", "ARTIKEL", "6 menit", R.drawable.phising),
        EducationItem("8", "Hukum Indonesia Tidak Tinggal Diam: Sanksi Pidana & Hak Ganti Rugi Korban Phishing", "Hukum", "ARTIKEL", "5 menit", R.drawable.bacaan_wajib),
        EducationItem("9", "Awas Penyusup Senyap! Bagaimana Malware Menguras Rekening Anda Tanpa Disadari", "Keamanan", "ARTIKEL", "5 menit", R.drawable.phising),
        EducationItem("10", "Jebakan Sosial Media: Trik Licik Penipu Menandai Anda di WhatsApp dan Instagram", "Rekayasa Sosial", "ARTIKEL", "4 menit", R.drawable.phising),
        EducationItem("11", "Panduan Khusus Pemilik Toko (Merchant): Amankan QRIS Anda dari Tangan Jahil!", "Bisnis", "ARTIKEL", "5 menit", R.drawable.qushing),
        EducationItem("12", "Mengintip Dapur Penetas Tautan Phishing: Seberapa Mudah Halaman Palsu Dibuat?", "Teknis", "ARTIKEL", "6 menit", R.drawable.phising),
        EducationItem("4", "Kuis: Bisakah Kamu Bedakan Mana yang Scam?", "Simulasi", "KUIS", "5 menit")
    )

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == "Semua") {
            allItems.filter { it.type != "BACAAN WAJIB" }
        } else {
            allItems.filter { it.category == selectedCategory && it.type != "BACAAN WAJIB" }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // Hero Header for Education
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
                            text = "Pusat Edukasi",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Literasi digital untuk perlindungan diri",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 110.dp)
        ) {
            // Featured Card
            item {
                FeaturedCard(
                    isCompleted = completedIds.contains(allItems[0].id),
                    onClick = { onItemClick(allItems[0]) }
                )
            }

            // Category Filters
            item {
                CategoryFilters(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            // Content List
            items(filteredItems) { item ->
                EducationCard(
                    item = item,
                    isCompleted = completedIds.contains(item.id),
                    onClick = { onItemClick(item) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FeaturedCard(
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(DeepNavy)
            .clickable { onClick() }
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bacaan_wajib),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )

        // Gradient Overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            DeepNavy.copy(alpha = 0.9f)
                        ),
                        startY = 100f
                    )
                )
        )
        
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selesai",
                tint = SafeGreen,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Cerulean)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("BACAAN WAJIB", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Panduan Lengkap Mengenali Penipuan Digital 2026",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
        }
    }
}


@Composable
private fun CategoryFilters(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Semua", "Phishing", "Keamanan", "Rekayasa Sosial", "QRIS Palsu", "Hukum", "Bisnis", "Teknis")
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                label = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Cerulean else DeepNavy.copy(alpha = 0.05f)
    val textColor = if (isSelected) Color.White else Slate500

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EducationCard(
    item: EducationItem,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DeepNavy.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageResId != null) {
                    Image(
                        painter = painterResource(id = item.imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(if (item.type == "KUIS") "📝" else "📄", fontSize = 24.sp)
                }
                
                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (item.type == "KUIS") WarningYellow.copy(alpha = 0.1f) else YaleBlue.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(item.type, color = if (item.type == "KUIS") WarningYellow else YaleBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item.duration, color = Slate500, fontSize = 11.sp)
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.title,
                    color = PrussianBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    lineHeight = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.category,
                    color = Cerulean,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selesai",
                    tint = SafeGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EducationCenterPreview() {
    EducationCenterScreen(completedIds = setOf("1"), onBack = {}, onItemClick = {})
}
