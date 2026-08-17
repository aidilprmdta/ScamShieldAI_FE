package com.example.scamshieldai.model

import com.example.scamshieldai.R

object EducationCatalog {
    val all: List<EducationContent> = listOf(
        EducationContent(
            id = "1",
            title = "Mengenali tautan phishing",
            category = "Phishing",
            duration = "3 menit",
            description = listOf(
                "Phishing adalah tipuan lewat pesan atau situs palsu yang mirip bank, e-commerce, atau instansi resmi. Tujuannya mencuri data login atau info keuangan.",
                "Biasanya tautan dikirim lewat SMS, WhatsApp, atau email. Setelah diklik, Anda diarahkan ke halaman tiruan yang hampir sama dengan aslinya."
            ),
            tips = listOf(
                "Periksa URL dengan teliti — domain resmi jarang pakai .xyz, .info, atau angka acak",
                "Bank dan e-commerce tidak pernah minta password lewat tautan",
                "Hati-hati pesan yang mendesak \"segera\" atau \"dalam 24 jam\"",
                "Cek dulu di ScamShield sebelum klik tautan yang mencurigakan"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "2",
            title = "Modus mama minta transfer",
            category = "Rekayasa Sosial",
            duration = "4 menit",
            description = listOf(
                "Rekayasa sosial memanfaatkan rasa percaya dan kepanikan. Modus \"mama minta transfer\" sering menyasar keluarga yang khawatir anggota rumah tangganya sedang kesulitan.",
                "Penipu mengaku saudara lewat nomor baru, bilang ada keadaan darurat, lalu meminta transfer segera."
            ),
            tips = listOf(
                "Verifikasi dengan menelepon nomor lama yang Anda kenal",
                "Tanyakan hal yang hanya diketahui anggota keluarga asli",
                "Jangan panik — desakan waktu memang bagian dari tipuannya",
                "Beritahu keluarga soal modus ini agar sama-sama waspada"
            ),
            imageResId = R.drawable.chatpalsu
        ),
        EducationContent(
            id = "3",
            title = "QRIS palsu dan quishing",
            category = "QRIS Palsu",
            duration = "3 menit",
            description = listOf(
                "Quishing memakai QR code untuk mengarahkan korban ke situs phishing. Penipu sering menempel QR palsu di atas QR resmi di restoran, parkiran, atau tempat umum.",
                "Setelah dipindai, Anda bisa dibawa ke halaman yang meminta data pribadi atau mengarahkan pembayaran ke rekening penipu."
            ),
            tips = listOf(
                "Periksa apakah QR terlihat ditempel di atas stiker lain",
                "Baca URL tujuan sebelum lanjut bayar atau isi data",
                "Pakai fitur Scan QR di ScamShield untuk cek dulu",
                "Di tempat umum, pilih bayar manual jika QR terlihat aneh"
            ),
            imageResId = R.drawable.qushing
        ),
        EducationContent(
            id = "0",
            title = "Dasar mengenali penipuan digital",
            category = "Keamanan",
            duration = "10 menit",
            description = listOf(
                "Penipuan online semakin mirip komunikasi resmi. Mulai dari pesan bank palsu sampai QR yang diganti diam-diam.",
                "Artikel ini merangkum kebiasaan sederhana agar data dan rekening Anda lebih aman: cek dulu, jangan buru-buru transfer, dan jangan bagikan OTP."
            ),
            tips = listOf(
                "Aktifkan 2FA di akun penting",
                "Jangan bagikan kode OTP kepada siapa pun",
                "Cek dulu di ScamShield jika pesan terasa mencurigakan"
            ),
            imageResId = R.drawable.bacaan_wajib
        ),
        EducationContent(
            id = "5",
            title = "Membedakan QRIS asli dan palsu",
            category = "QRIS Palsu",
            duration = "5 menit",
            description = listOf(
                "Quishing sering memakai stiker QR palsu yang ditempel di atas QRIS asli. Setelah dipindai, korban diarahkan ke situs atau rekening penipu.",
                "Pada QRIS asli, nama merchant di aplikasi harus sama dengan yang tertulis di tempat usaha. QRIS asli juga menampilkan NMID dan Terminal ID; yang palsu sering tidak punya data itu."
            ),
            tips = listOf(
                "Periksa apakah stiker QRIS ditumpuk atau ditempel ulang",
                "Pastikan nama merchant di aplikasi sama dengan di papan fisik",
                "Scan QRIS lewat aplikasi pembayaran, bukan browser sembarangan",
                "Gunakan menu scan banking/e-wallet, bukan kamera biasa"
            ),
            imageResId = R.drawable.qushing
        ),
        EducationContent(
            id = "6",
            title = "Langkah jika sudah klik tautan mencurigakan",
            category = "Keamanan",
            duration = "4 menit",
            description = listOf(
                "Kalau sudah terlanjur klik tautan aneh atau unduh file mencurigakan (misalnya APK palsu), jangan panik. Ada langkah cepat untuk membatasi kerusakan.",
                "Pertama, putus koneksi internet. Matikan Wi-Fi atau data seluler agar malware sulit mengirim data keluar."
            ),
            tips = listOf(
                "Matikan koneksi internet (Wi-Fi/data)",
                "Ganti kata sandi dan PIN banking serta email",
                "Jalankan pemindaian antivirus tepercaya",
                "Cadangkan data penting ke cloud atau drive eksternal",
                "Laporkan ke bank terkait dan pihak berwenang"
            ),
            imageResId = R.drawable.scamp_no_palsu
        ),
        EducationContent(
            id = "7",
            title = "Jenis-jenis phishing",
            category = "Phishing",
            duration = "6 menit",
            description = listOf(
                "Phishing tidak hanya lewat email. Ada Smishing (SMS), Vishing (telepon), dan Clone Phishing yang memakai salinan pesan resmi yang sudah diubah.",
                "Bentuknya berbeda, polanya mirip: meniru pihak tepercaya, menciptakan rasa takut atau buru-buru, lalu meminta data atau uang."
            ),
            tips = listOf(
                "Waspadai file APK palsu seperti undangan digital atau resi paket",
                "Jangan mudah percaya panggilan yang mengaku dari bank",
                "Hati-hati pharming yang mengalihkan browser ke situs palsu",
                "Cek keaslian pesan meski tampilannya mirip email resmi"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "8",
            title = "Sanksi hukum dan hak korban phishing",
            category = "Hukum",
            duration = "5 menit",
            description = listOf(
                "Pelaku phishing bisa dijerat UU ITE dan UU Perlindungan Data Pribadi. Korban juga punya jalur untuk melapor dan meminta ganti rugi.",
                "Berdasarkan UU PDP No. 27 Tahun 2022, pengambilan data pribadi secara ilegal diancam pidana hingga 5 tahun dan denda hingga Rp5 miliar."
            ),
            tips = listOf(
                "Pahami hak Anda sebagai pemilik data pribadi",
                "Laporkan kasus ke BSSN atau polisi (cyber crime)",
                "Korban bisa mengajukan restitusi lewat LPSK",
                "Simpan bukti chat dan tautan untuk laporan"
            ),
            imageResId = R.drawable.bacaan_wajib
        ),
        EducationContent(
            id = "9",
            title = "Malware yang menguras rekening",
            category = "Keamanan",
            duration = "5 menit",
            description = listOf(
                "Malware adalah perangkat lunak berbahaya yang berjalan di latar belakang. Berbeda dari phishing biasa, malware bisa mencuri data tanpa Anda sadari.",
                "Sering masuk lewat APK tidak resmi, tautan resi palsu, atau undangan digital mencurigakan. Setelah terpasang, akses ke aplikasi banking bisa disalahgunakan."
            ),
            tips = listOf(
                "Jangan unduh file APK dari sumber tidak resmi",
                "Lakukan pemindaian perangkat secara berkala",
                "Hindari tautan resi atau undangan dari nomor tak dikenal",
                "Cek dulu di ScamShield jika file atau pesan terasa aneh"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "10",
            title = "Penipuan lewat WhatsApp dan Instagram",
            category = "Rekayasa Sosial",
            duration = "4 menit",
            description = listOf(
                "Di media sosial, penipu sering pakai DM hadiah palsu, komentar berisi tautan, atau halaman login tiruan.",
                "Tujuannya membuat Anda klik, isi data, atau mengizinkan akses akun. Kalau tawaran terlalu bagus atau mendesak, anggap itu sinyal bahaya."
            ),
            tips = listOf(
                "Abaikan DM dari akun asing yang menjanjikan hadiah besar",
                "Jangan klik tautan \"info lengkap\" di komentar publik",
                "Aktifkan 2FA di akun sosial media",
                "Hati-hati halaman login tiruan yang mirip Facebook/Instagram"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "11",
            title = "Mengamankan QRIS toko",
            category = "Bisnis",
            duration = "5 menit",
            description = listOf(
                "Untuk merchant, keamanan QRIS termasuk urusan fisik di meja kasir. Penipu bisa menempel stiker baru di atas QR toko saat ramai atau kasir lengah.",
                "Cek stiker secara rutin dan pastikan notifikasi pembayaran masuk di sistem merchant Anda sendiri, bukan hanya dari screenshot pelanggan."
            ),
            tips = listOf(
                "Periksa stiker QRIS setiap pagi dan sore",
                "Minta karyawan mengawasi proses bayar sampai selesai",
                "Jangan hanya percaya screenshot bukti bayar",
                "Pastikan notifikasi sukses muncul di sistem merchant"
            ),
            imageResId = R.drawable.qushing
        ),
        EducationContent(
            id = "12",
            title = "Bagaimana halaman phishing dibuat",
            category = "Teknis",
            duration = "6 menit",
            description = listOf(
                "Tampilan bagus tidak menjamin situs asli. Penipu bisa menyalin halaman login bank atau media sosial dengan alat siap pakai.",
                "Karena itu, selalu cek ejaan domain dan jangan mengisi kata sandi hanya karena desainnya terlihat resmi."
            ),
            tips = listOf(
                "Jangan mengandalkan desain saja untuk menilai keaslian",
                "Periksa ejaan URL (misal: bca.co.id vs bca-verifikasi.xyz)",
                "Pakai pengelola kata sandi yang hanya mengisi di situs asli",
                "Laporkan situs phishing ke ID-CERT atau penyedia layanan terkait"
            ),
            imageResId = R.drawable.phising
        )
    )

    fun findById(id: String?): EducationContent =
        all.find { it.id == id } ?: all.first()
}
