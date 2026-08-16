package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.components.ScreenTopBar
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
    onBack: () -> Unit,
    onItemClick: (EducationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Semua") }

    val allItems = listOf(
        EducationItem("0", "Dasar mengenali penipuan digital", "Keamanan", "BACAAN WAJIB", "10 menit", R.drawable.bacaan_wajib),
        EducationItem("1", "Mengenali tautan phishing", "Phishing", "ARTIKEL", "3 menit", R.drawable.phising),
        EducationItem("2", "Modus mama minta transfer", "Rekayasa Sosial", "ARTIKEL", "4 menit", R.drawable.chatpalsu),
        EducationItem("3", "QRIS palsu dan quishing", "QRIS Palsu", "ARTIKEL", "3 menit", R.drawable.qushing),
        EducationItem("5", "Membedakan QRIS asli dan palsu", "QRIS Palsu", "ARTIKEL", "5 menit", R.drawable.qushing),
        EducationItem("6", "Langkah jika sudah klik tautan mencurigakan", "Keamanan", "ARTIKEL", "4 menit", R.drawable.scamp_no_palsu),
        EducationItem("7", "Jenis-jenis phishing", "Phishing", "ARTIKEL", "6 menit", R.drawable.phising),
        EducationItem("8", "Sanksi hukum dan hak korban phishing", "Hukum", "ARTIKEL", "5 menit", R.drawable.bacaan_wajib),
        EducationItem("9", "Malware yang menguras rekening", "Keamanan", "ARTIKEL", "5 menit", R.drawable.phising),
        EducationItem("10", "Penipuan lewat WhatsApp dan Instagram", "Rekayasa Sosial", "ARTIKEL", "4 menit", R.drawable.phising),
        EducationItem("11", "Mengamankan QRIS toko", "Bisnis", "ARTIKEL", "5 menit", R.drawable.qushing),
        EducationItem("12", "Bagaimana halaman phishing dibuat", "Teknis", "ARTIKEL", "6 menit", R.drawable.phising),
        EducationItem("4", "Kuis: bedakan pesan scam", "Simulasi", "KUIS", "5 menit")
    )

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == "Semua") {
            allItems.filter { it.type != "BACAAN WAJIB" }
        } else {
            allItems.filter { it.category == selectedCategory && it.type != "BACAAN WAJIB" }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.baground_edukasi),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            ScreenTopBar(
                title = "Edukasi",
                subtitle = "Artikel dan kuis singkat",
                onBack = onBack,
                isHero = true
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 110.dp)
            ) {
                item {
                    FeaturedCard(
                        onClick = { onItemClick(allItems[0]) }
                    )
                }

                item {
                    CategoryFilters(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

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
}

@Composable
private fun FeaturedCard(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(DeepNavy)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bacaan_wajib),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(DeepNavy)
                .padding(20.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Cerulean)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Panduan", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dasar mengenali penipuan digital",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            }
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
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            UnderlineFilter(
                label = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun UnderlineFilter(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Text(
            text = label,
            color = if (isSelected) Cerulean else Slate500,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(if (isSelected) Cerulean else Color.Transparent)
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
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .border(1.dp, YaleBlue.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                    Icon(
                        imageVector = if (item.type == "KUIS") Icons.Default.Quiz else Icons.Default.Article,
                        contentDescription = null,
                        tint = Cerulean,
                        modifier = Modifier.size(28.dp)
                    )
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EducationCenterPreview() {
    EducationCenterScreen(onBack = {}, onItemClick = {})
}
