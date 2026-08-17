package com.example.scamshieldai.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.theme.*

private data class HelpFaq(
    val question: String,
    val answer: String
)

private data class HelpCategory(
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val faqs: List<HelpFaq>
)

@Composable
fun HelpCenterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember {
        listOf(
            HelpCategory(
                title = "Akun",
                icon = Icons.Outlined.Person,
                iconTint = YaleBlue,
                faqs = listOf(
                    HelpFaq(
                        question = "Cara daftar?",
                        answer = "Buka Daftar, isi email dan password (minimal 6 karakter), lalu ketuk Daftar. Bisa juga lewat Google."
                    ),
                    HelpFaq(
                        question = "Lupa password?",
                        answer = "Kalau login pakai Google, pulihkan lewat akun Google. Kalau email/password, hubungi dukungan ScamShield. Setelah masuk, ubah password di Profil → Keamanan."
                    ),
                    HelpFaq(
                        question = "Ubah password?",
                        answer = "Profil → Keamanan → Ubah Kata Sandi. Hanya untuk akun email/password."
                    ),
                    HelpFaq(
                        question = "Ubah nama atau email?",
                        answer = "Profil → Edit Profil, ubah data, lalu Simpan."
                    ),
                    HelpFaq(
                        question = "Harus login dulu buat scan?",
                        answer = "Tidak. Tapi riwayat, laporan, dan notifikasi status butuh akun yang sudah masuk."
                    )
                )
            ),
            HelpCategory(
                title = "Scan chat & screenshot",
                icon = Icons.Outlined.Chat,
                iconTint = Cerulean,
                faqs = listOf(
                    HelpFaq(
                        question = "Cara cek chat mencurigakan?",
                        answer = "Di Beranda pilih Scan Chat, tempel atau ketik isinya, lalu jalankan. Hasilnya skor risiko plus penjelasan singkat."
                    ),
                    HelpFaq(
                        question = "Bedanya Scan Chat dan Screenshot?",
                        answer = "Scan Chat untuk teks yang diketik/ditempel. Screenshot membaca teks dari gambar dulu, lalu dicek sama seperti teks biasa."
                    ),
                    HelpFaq(
                        question = "Kenapa hasilnya risiko tinggi?",
                        answer = "Biasanya ada pola seperti desakan waktu, minta OTP/transfer, atau tautan aneh. Baca penjelasan di hasil sebelum bertindak."
                    )
                )
            ),
            HelpCategory(
                title = "Link & QR",
                icon = Icons.Outlined.Link,
                iconTint = SafeGreen,
                faqs = listOf(
                    HelpFaq(
                        question = "Cara cek tautan?",
                        answer = "Pilih Cek Tautan di Beranda, tempel URL lengkap, lalu jalankan."
                    ),
                    HelpFaq(
                        question = "Scan QR langsung buka situs?",
                        answer = "Tidak. QR dibaca dulu, isinya dicek, baru Anda putuskan mau buka atau tidak."
                    ),
                    HelpFaq(
                        question = "QRIS toko kelihatan aneh?",
                        answer = "Jangan bayar dulu. Cek apakah stiker ditempel di atas yang lain, dan cocokkan nama merchant di aplikasi bayar dengan toko fisik."
                    )
                )
            ),
            HelpCategory(
                title = "Riwayat & laporan",
                icon = Icons.Outlined.History,
                iconTint = WarningYellow,
                faqs = listOf(
                    HelpFaq(
                        question = "Di mana hasil scan lama?",
                        answer = "Tab Riwayat. Muncul kalau Anda login saat scan."
                    ),
                    HelpFaq(
                        question = "Cara laporkan penipuan?",
                        answer = "Dari hasil analisis pilih Laporkan, isi kategori dan detail. Statusnya di Profil → Laporan Saya."
                    ),
                    HelpFaq(
                        question = "Arti pending / verified / rejected?",
                        answer = "Pending: masih ditinjau. Verified: diterima. Rejected: ditolak."
                    )
                )
            ),
            HelpCategory(
                title = "Edukasi & data",
                icon = Icons.Outlined.School,
                iconTint = DeepNavy,
                faqs = listOf(
                    HelpFaq(
                        question = "Isi menu Edukasi?",
                        answer = "Artikel dan kuis soal phishing, QRIS palsu, dan tip keamanan."
                    ),
                    HelpFaq(
                        question = "Data scan aman?",
                        answer = "Dikirim lewat HTTPS. Jangan masukkan password, PIN, atau OTP ke kolom scan."
                    ),
                    HelpFaq(
                        question = "Atur notifikasi?",
                        answer = "Profil → Notifikasi. Bisa dimatikan semua atau per kategori."
                    )
                )
            )
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .statusBarsPadding()
    ) {
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
                    .background(DeepNavy.copy(alpha = 0.05f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = PrussianBlue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Pusat Bantuan",
                    color = PrussianBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pertanyaan umum",
                    color = Slate500,
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Ketuk kategori, lalu ketuk pertanyaan untuk melihat jawaban.",
                color = Slate500,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            categories.forEach { category ->
                HelpCategorySection(category = category)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Jangan bagikan OTP, PIN, atau password lewat chat.",
                color = Slate400,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun HelpCategorySection(category: HelpCategory) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardWhite,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.05f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(category.iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(category.icon, contentDescription = null, tint = category.iconTint, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = category.title,
                    color = PrussianBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Slate400
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(thickness = 0.5.dp, color = Slate100)
                    category.faqs.forEachIndexed { index, faq ->
                        HelpFaqItem(faq = faq)
                        if (index < category.faqs.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Slate100
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpFaqItem(faq: HelpFaq) {
    var open by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { open = !open }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = faq.question,
                color = PrussianBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
            Icon(
                imageVector = if (open) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Slate400,
                modifier = Modifier.size(20.dp)
            )
        }
        AnimatedVisibility(visible = open) {
            Text(
                text = faq.answer,
                color = Slate500,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 8.dp, end = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpCenterPreview() {
    HelpCenterScreen(onBack = {})
}
