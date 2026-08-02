package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDeepNavy = Color(0xFF0B1628)
private val CardBg = Color(0xFF1E293B).copy(alpha = 0.4f)
private val TealAccent = Color(0xFF2DD4BF)
private val Slate400 = Color(0xFF94A3B8)
private val Slate500 = Color(0xFF64748B)

data class EducationItem(
    val id: String,
    val title: String,
    val category: String,
    val type: String, // "ARTIKEL", "KUIS", "BACAAN WAJIB"
    val duration: String,
    val imageUrl: String? = null
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
        EducationItem("0", "Panduan Lengkap Mengenali Penipuan Digital 2026", "Keamanan", "BACAAN WAJIB", "10 menit"),
        EducationItem("1", "Waspada Phishing: Kenali Tautan Palsu", "Phishing", "ARTIKEL", "3 menit"),
        EducationItem("2", "Modus \"Mama Minta Transfer\" — Rekayasa Sosial", "Rekayasa Sosial", "ARTIKEL", "4 menit"),
        EducationItem("3", "QRIS Palsu & Quishing — Bahaya di Balik QR Code", "QRIS Palsu", "ARTIKEL", "3 menit"),
        EducationItem("4", "Kuis: Bisakah Kamu Bedakan Mana yang Scam?", "Simulasi", "KUIS", "5 menit")
    )

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == "Semua") {
            allItems.filter { it.type != "BACAAN WAJIB" }
        } else {
            allItems.filter { it.category == selectedCategory && it.type != "BACAAN WAJIB" }
        }
    }

    val progressItems = remember { allItems.filter { it.type == "ARTIKEL" } }
    val completedProgressCount = remember(completedIds) {
        progressItems.count { completedIds.contains(it.id) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDeepNavy)
            .statusBarsPadding()
    ) {
        // ... (Header remains the same)
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Pusat Edukasi",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Literasi digital untuk perlindungan diri",
                    color = Slate400,
                    fontSize = 14.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Featured Card
            item {
                FeaturedCard(
                    onClick = { onItemClick(allItems[0]) }
                )
            }

            // Progress Section
            item {
                ProgressSection(
                    completedCount = completedProgressCount,
                    totalCount = progressItems.size
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
                    onClick = { onItemClick(item) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FeaturedCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Gray.copy(alpha = 0.2f)) // Placeholder for image
            .clickable { onClick() }
    ) {
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(TealAccent.copy(alpha = 0.4f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("BACAAN WAJIB", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
private fun ProgressSection(completedCount: Int, totalCount: Int) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Progress Literasi", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("$completedCount / $totalCount selesai", color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = TealAccent,
                trackColor = Color.White.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Selesaikan semua materi untuk jadi pengguna digital yang aman",
                color = Slate400,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun CategoryFilters(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Semua", "Phishing", "Rekayasa Sosial", "QRIS Palsu")
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
    val bgColor = if (isSelected) TealAccent else Color.White.copy(alpha = 0.1f)
    val textColor = if (isSelected) Color(0xFF0B1628) else Slate400

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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (item.type == "KUIS") "📝" else "📄", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (item.type == "KUIS") Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF7C3AED).copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(item.type, color = if (item.type == "KUIS") Color(0xFFF59E0B) else Color(0xFFA78BFA), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item.duration, color = Slate500, fontSize = 11.sp)
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    lineHeight = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.category,
                    color = TealAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
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
