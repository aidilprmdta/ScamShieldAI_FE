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
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
                title = "Memulai & Akun",
                icon = Icons.Outlined.Person,
                iconTint = YaleBlue,
                faqs = listOf(
                    HelpFaq(
                        question = "Bagaimana cara membuat akun?",
                        answer = "Buka halaman Sign Up, masukkan email dan password (minimal 6 karakter), lalu ketuk Sign Up. Anda juga bisa mendaftar cepat dengan tombol Continue with Google."
                    ),
                    HelpFaq(
                        question = "Saya lupa password, apa yang harus dilakukan?",
                        answer = "Saat ini reset password belum tersedia di aplikasi. Gunakan login Google jika akun Anda terhubung ke Google, atau hubungi tim dukungan ScamShield AI untuk bantuan pemulihan akun."
                    ),
                    HelpFaq(
                        question = "Bagaimana cara mengubah nama atau email?",
                        answer = "Buka Profil → Edit Profil. Ubah nama tampilan dan/atau email, lalu ketuk Simpan Perubahan. Perubahan akan langsung terlihat di halaman profil."
                    ),
                    HelpFaq(
                        question = "Apakah saya harus login untuk menganalisis chat/link?",
                        answer = "Analisis bisa dilakukan tanpa login. Namun untuk menyimpan riwayat scan, mengelola laporan, dan menerima notifikasi status laporan, Anda perlu login terlebih dahulu."
                    )
                )
            ),
            HelpCategory(
                title = "Scan Chat & Screenshot",
                icon = Icons.Outlined.Chat,
                iconTint = Cerulean,
                faqs = listOf(
                    HelpFaq(
                        question = "Bagaimana cara menganalisis chat mencurigakan?",
                        answer = "Dari Beranda, pilih Scan Chat. Tempel atau ketik isi pesan (SMS/WhatsApp/email), lalu ketuk analisis. ScamShield AI akan menilai skor risiko, menandai red flags, dan memberi rekomendasi."
                    ),
                    HelpFaq(
                        question = "Apa bedanya Scan Chat dan Scan Screenshot?",
                        answer = "Scan Chat untuk teks yang Anda ketik/tempel langsung. Scan Screenshot memakai OCR untuk membaca teks dari gambar chat, lalu menganalisisnya dengan cara yang sama."
                    ),
                    HelpFaq(
                        question = "Mengapa hasil scan menampilkan risiko tinggi?",
                        answer = "AI mendeteksi pola umum penipuan seperti urgensi berlebih, permintaan OTP/transfer, tautan mencurigakan, atau bahasa rekayasa sosial. Baca penjelasan dan red flags pada hasil analisis sebelum bertindak."
                    )
                )
            ),
            HelpCategory(
                title = "Cek Link & QR",
                icon = Icons.Outlined.Link,
                iconTint = SafeGreen,
                faqs = listOf(
                    HelpFaq(
                        question = "Bagaimana cara mengecek tautan?",
                        answer = "Pilih Cek Link di Beranda, tempel URL lengkap (termasuk https:// bila ada), lalu jalankan analisis. Sistem memeriksa reputasi tautan dan konteks penipuan."
                    ),
                    HelpFaq(
                        question = "Apakah Scan QR aman digunakan?",
                        answer = "Ya. Pilih Scan QR, izinkan akses kamera, lalu arahkan ke kode QR. Aplikasi membaca isi QR tanpa langsung membuka situs, lalu menganalisis kontennya terlebih dahulu."
                    ),
                    HelpFaq(
                        question = "QRIS di toko terlihat mencurigakan, apa yang harus saya lakukan?",
                        answer = "Jangan langsung bayar. Scan dulu dengan ScamShield AI. Periksa apakah stiker QR ditempel di atas yang lain, dan pastikan nama merchant di aplikasi pembayaran cocok dengan toko fisik."
                    )
                )
            ),
            HelpCategory(
                title = "Riwayat & Laporan",
                icon = Icons.Outlined.History,
                iconTint = WarningYellow,
                faqs = listOf(
                    HelpFaq(
                        question = "Di mana saya melihat hasil scan sebelumnya?",
                        answer = "Buka tab Riwayat di navigasi bawah. Setiap analisis yang berhasil disimpan (saat Anda login) muncul di sana dan bisa dibuka ulang atau dihapus."
                    ),
                    HelpFaq(
                        question = "Bagaimana cara melaporkan konten penipuan?",
                        answer = "Dari hasil analisis, pilih opsi Laporkan, atau gunakan fitur laporan komunitas. Isi tipe, konten, dan catatan bila perlu. Status laporan bisa dipantau di Profil → Laporan Saya."
                    ),
                    HelpFaq(
                        question = "Apa arti status verified / rejected / pending?",
                        answer = "Pending berarti menunggu tinjauan. Verified berarti laporan diterima/diverifikasi admin. Rejected berarti laporan ditolak. Anda mendapat notifikasi saat status berubah (jika notifikasi diaktifkan)."
                    )
                )
            ),
            HelpCategory(
                title = "Edukasi & Keamanan",
                icon = Icons.Outlined.School,
                iconTint = Color(0xFFA855F7),
                faqs = listOf(
                    HelpFaq(
                        question = "Apa isi menu Edukasi?",
                        answer = "Pusat edukasi berisi artikel dan kuis tentang phishing, rekayasa sosial, QRIS palsu, malware, dan tips keamanan digital agar Anda lebih waspada di luar aplikasi."
                    ),
                    HelpFaq(
                        question = "Apakah data scan saya aman?",
                        answer = "Analisis dikirim ke server secara terenkripsi (HTTPS pada produksi). Riwayat tersimpan untuk akun Anda. Jangan masukkan password, PIN, atau kode OTP ke kolom scan."
                    ),
                    HelpFaq(
                        question = "Bagaimana mengatur notifikasi?",
                        answer = "Buka Profil → Notifikasi. Anda bisa mengaktifkan/nonaktifkan semua notifikasi atau per kategori (peringatan keamanan, edukasi, pengumuman sistem)."
                    )
                )
            ),
            HelpCategory(
                title = "Masalah Umum",
                icon = Icons.Outlined.Security,
                iconTint = DangerRed,
                faqs = listOf(
                    HelpFaq(
                        question = "Aplikasi tidak bisa login / register?",
                        answer = "Pastikan backend berjalan dan perangkat terhubung ke jaringan yang sama (untuk development). Periksa email & password, lalu coba lagi. Jika memakai Google Sign-In, pastikan akun Google aktif."
                    ),
                    HelpFaq(
                        question = "Riwayat kosong padahal sudah scan?",
                        answer = "Pastikan Anda sudah login saat menganalisis. Tanpa login, hasil tetap tampil setelah scan tetapi mungkin tidak tersimpan ke riwayat akun. Coba pull-to-refresh di halaman Riwayat."
                    ),
                    HelpFaq(
                        question = "Notifikasi tidak muncul?",
                        answer = "Izinkan notifikasi di pengaturan sistem Android untuk ScamShield AI, lalu aktifkan juga di Profil → Notifikasi. Login ulang agar token FCM terdaftar ulang ke server."
                    ),
                    HelpFaq(
                        question = "Siapa yang bisa saya hubungi?",
                        answer = "Untuk pertanyaan terkait aplikasi atau laporan penipuan yang mendesak, hubungi tim ScamShield AI melalui email dukungan di halaman Tentang, atau laporkan kasus ke pihak berwenang (BSSN / polisi siber) bila kerugian finansial terjadi."
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
                    text = "FAQ & panduan penggunaan",
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = YaleBlue.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, YaleBlue.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Butuh bantuan cepat?",
                        color = PrussianBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Pilih kategori di bawah, lalu ketuk pertanyaan untuk melihat jawabannya. Konten ini membantu Anda memakai ScamShield AI dengan aman dan efektif.",
                        color = Slate500,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            categories.forEach { category ->
                HelpCategorySection(category = category)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tips: jangan pernah membagikan OTP, PIN, atau password melalui chat yang dianalisis di aplikasi ini.",
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = YaleBlue.copy(alpha = 0.06f)),
        color = CardWhite,
        shape = RoundedCornerShape(20.dp),
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
