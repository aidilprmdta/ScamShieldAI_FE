package com.example.scamshieldai.model

import com.example.scamshieldai.R

object EducationCatalog {
    val all: List<EducationContent> = listOf(
        EducationContent(
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
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "2",
            title = "Modus \"Mama Minta Transfer\" — Rekayasa Sosial",
            category = "Rekayasa Sosial",
            duration = "4 menit",
            description = listOf(
                "Rekayasa sosial (social engineering) adalah manipulasi psikologis yang mengeksploitasi kepercayaan dan rasa sayang kita. Modus \"mama minta transfer\" adalah salah satu yang paling sering menyasar orang tua dan lansia di Indonesia.",
                "Penipu menghubungi korban dengan berpura-pura sebagai anggota keluarga yang dalam kesulitan, menggunakan nomor baru, dan meminta transfer uang segera."
            ),
            tips = listOf(
                "Selalu verifikasi dengan menelpon langsung ke nomor lama yang Anda kenal",
                "Tanyakan informasi yang hanya diketahui anggota keluarga asli",
                "Jangan panik — penipu sengaja menciptakan situasi mendesak",
                "Beritahu anggota keluarga tentang modus ini agar mereka waspada"
            ),
            imageResId = R.drawable.chatpalsu
        ),
        EducationContent(
            id = "3",
            title = "QRIS Palsu & Quishing — Bahaya di Balik QR Code",
            category = "QRIS Palsu",
            duration = "3 menit",
            description = listOf(
                "Quishing (QR phishing) adalah modus penipuan menggunakan QR code untuk mengarahkan korban ke situs phishing. Penipu menempelkan QR code palsu di atas QR code resmi di restoran, parkiran, atau tempat umum lainnya.",
                "Ketika Anda memindai QR palsu tersebut, Anda akan dibawa ke situs yang meminta data pribadi atau melakukan pembayaran ke rekening penipu."
            ),
            tips = listOf(
                "Selalu periksa apakah QR code terlihat asli atau ditempel di atas yang lain",
                "Baca URL tujuan QR sebelum melakukan tindakan apapun",
                "Gunakan fitur Scan QR di ScamShield AI untuk verifikasi",
                "Di tempat umum, pilih bayar manual jika QR terlihat mencurigakan"
            ),
            imageResId = R.drawable.qushing
        ),
        EducationContent(
            id = "0",
            title = "Panduan Lengkap Mengenali Penipuan Digital 2026",
            category = "Keamanan",
            duration = "10 menit",
            description = listOf(
                "Selamat datang di panduan komprehensif keamanan digital tahun 2026. Di era kecerdasan buatan, modus penipuan menjadi semakin canggih dan sulit dibedakan dari komunikasi resmi.",
                "Panduan ini merangkum teknik perlindungan diri paling mutakhir untuk menjaga data dan aset finansial Anda di dunia maya."
            ),
            tips = listOf(
                "Aktifkan 2FA (Two-Factor Authentication) di semua akun",
                "Jangan pernah membagikan kode OTP kepada siapa pun",
                "Gunakan ScamShield AI untuk memverifikasi setiap pesan mencurigakan"
            ),
            imageResId = R.drawable.bacaan_wajib
        ),
        EducationContent(
            id = "5",
            title = "Jangan Asal Scan! Cara Cerdas Membedakan QRIS Asli vs QRIS Palsu",
            category = "QRIS Palsu",
            duration = "5 menit",
            description = listOf(
                "Bahaya \"Quishing\" (QR Code Phishing): Modus penipuan ini menggunakan stiker QR Code palsu yang ditempelkan oleh oknum penipu di atas stiker QRIS asli. Saat dipindai, korban akan diarahkan ke situs palsu untuk mencuri data pribadi atau langsung menguras dana rekening.",
                "Nama Merchant: Pada QRIS asli, nama merchant yang tertera di papan fisik harus sama persis dengan nama merchant yang muncul di layar aplikasi pembayaran saat dipindai. Pada QRIS palsu, nama yang muncul sering kali berbeda.",
                "National Merchant ID (NMID) & Terminal ID: QRIS asli selalu menampilkan nomor NMID dan Terminal ID (TID) di bagian bawah nama merchant. QRIS palsu umumnya tidak memiliki informasi ini."
            ),
            tips = listOf(
                "Periksa apakah stiker QRIS ditumpuk atau ditempel di atas yang lain",
                "Pastikan nama merchant di aplikasi sama dengan di papan fisik",
                "Barcode QRIS asli hanya dipindai via aplikasi pembayaran, bukan browser",
                "Gunakan menu scan bawaan aplikasi banking/e-wallet, bukan kamera biasa"
            ),
            imageResId = R.drawable.qushing
        ),
        EducationContent(
            id = "6",
            title = "Terlanjur Klik Link Mencurigakan? Lakukan 5 Langkah Penyelamatan Darurat!",
            category = "Keamanan",
            duration = "4 menit",
            description = listOf(
                "Jika Anda secara tidak sengaja mengklik tautan mencurigakan atau mengunduh berkas berbahaya (seperti file format .APK palsu), jangan panik. Ada langkah darurat yang bisa menyelamatkan data Anda.",
                "Langkah pertama adalah memutus koneksi internet. Segera matikan Wi-Fi atau cabut kabel internet. Ini mencegah malware mengirimkan data pribadi Anda keluar atau membatasi akses peretas."
            ),
            tips = listOf(
                "Matikan segera koneksi internet (Wi-Fi/Data)",
                "Ganti semua kata sandi dan PIN aplikasi perbankan & email",
                "Jalankan pemindaian antivirus tepercaya",
                "Cadangkan data penting ke cloud atau drive eksternal",
                "Laporkan ke bank terkait dan pihak berwenang"
            ),
            imageResId = R.drawable.scamp_no_palsu
        ),
        EducationContent(
            id = "7",
            title = "Mengenal 7 Wajah Phishing: Jangan Terkecoh Modus yang Mengintai Anda!",
            category = "Phishing",
            duration = "6 menit",
            description = listOf(
                "Phishing memiliki banyak variasi modus dan tidak hanya terbatas pada pesan email. Penipu terus berinovasi untuk menjebak korban melalui berbagai platform komunikasi.",
                "Mulai dari Smishing (SMS Phishing), Voice Phishing (Vishing), hingga Clone Phishing yang menggunakan salinan email resmi yang telah dimodifikasi."
            ),
            tips = listOf(
                "Waspadai file .APK palsu seperti undangan digital atau resi paket",
                "Jangan mudah percaya pada panggilan telepon yang mengaku pegawai resmi bank",
                "Hati-hati dengan 'Pharming' yang membelokkan trafik browser ke situs palsu",
                "Selidiki keaslian pesan meskipun terlihat seperti salinan email resmi"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "8",
            title = "Hukum Indonesia Tidak Tinggal Diam: Sanksi Pidana & Hak Ganti Rugi Korban Phishing",
            category = "Hukum",
            duration = "5 menit",
            description = listOf(
                "Pelaku phishing dapat dipidana berat berdasarkan UU ITE dan UU Perlindungan Data Pribadi (PDP). Indonesia memiliki regulasi kuat untuk menjerat para penjahat siber ini.",
                "Berdasarkan UU PDP No. 27 Tahun 2022, mengambil data pribadi secara ilegal diancam pidana penjara hingga 5 tahun dan denda hingga Rp5 miliar."
            ),
            tips = listOf(
                "Pahami hak Anda sebagai pemilik data pribadi berdasarkan UU PDP",
                "Laporkan kasus ke BSSN atau Kepolisian (Cyber Crime)",
                "Korban berhak mengajukan permohonan Restitusi (ganti rugi) melalui LPSK",
                "Simpan semua bukti chat/tautan sebagai barang bukti hukum"
            ),
            imageResId = R.drawable.bacaan_wajib
        ),
        EducationContent(
            id = "9",
            title = "Awas Penyusup Senyap! Bagaimana Malware Menguras Rekening Anda Tanpa Disadari",
            category = "Keamanan",
            duration = "5 menit",
            description = listOf(
                "Malware adalah perangkat lunak berbahaya yang bekerja senyap di latar belakang. Berbeda dengan phishing biasa, malware mampu mencuri data tanpa Anda sadari sama sekali.",
                "Menurut data BSSN, serangan malware menyumbang 15% dari total ancaman siber di sektor perbankan Indonesia. Sektor keuangan menjadi target utama karena potensi aset yang besar."
            ),
            tips = listOf(
                "Jangan pernah mengunduh file .APK dari sumber tidak resmi",
                "Lakukan pemindaian perangkat secara berkala",
                "Hindari mengklik tautan resi atau undangan dari nomor tidak dikenal",
                "Gunakan ScamShield AI untuk memverifikasi file mencurigakan"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "10",
            title = "Jebakan Sosial Media: Trik Licik Penipu Menandai Anda di WhatsApp dan Instagram",
            category = "Rekayasa Sosial",
            duration = "4 menit",
            description = listOf(
                "Media sosial merupakan ladang subur bagi penipu karena minimnya filter. Laporan IDADX 2023 menunjukkan phishing di media sosial mencapai 64% dari total serangan.",
                "Modus operandi biasanya melalui DM (Direct Message) dengan hadiah menggiurkan atau komentar yang memicu rasa penasaran untuk menjebak pengguna ke login palsu."
            ),
            tips = listOf(
                "Abaikan DM dari akun asing yang menawarkan hadiah besar",
                "Jangan klik link 'informasi lengkap' di kolom komentar postingan publik",
                "Aktifkan 2FA (Two-Factor Authentication) di semua akun sosial media",
                "Hati-hati dengan login page tiruan yang mirip Facebook/Instagram"
            ),
            imageResId = R.drawable.phising
        ),
        EducationContent(
            id = "11",
            title = "Panduan Khusus Pemilik Toko (Merchant): Amankan QRIS Anda dari Tangan Jahil!",
            category = "Bisnis",
            duration = "5 menit",
            description = listOf(
                "Keamanan transaksi QRIS adalah tanggung jawab bersama antara merchant dan konsumen. Sebagai pemilik toko, Anda harus waspada terhadap sabotase fisik pada kode QRIS Anda.",
                "Penipu seringkali menempelkan stiker baru di atas stiker lama milik toko Anda saat suasana sedang ramai atau kasir sedang lengah."
            ),
            tips = listOf(
                "Lakukan pemeriksaan fisik stiker QRIS secara berkala (setiap pagi/sore)",
                "Edukasi karyawan untuk mengawasi langkah pembayaran konsumen hingga tuntas",
                "Jangan hanya percaya pada screenshot bukti bayar dari konsumen",
                "Pastikan ada notifikasi sukses masuk di sistem merchant Anda sendiri"
            ),
            imageResId = R.drawable.qushing
        ),
        EducationContent(
            id = "12",
            title = "Mengintip Dapur Penetas Tautan Phishing: Seberapa Mudah Halaman Palsu Dibuat?",
            category = "Teknis",
            duration = "6 menit",
            description = listOf(
                "Tampilan visual website tidak menjamin keaslian. Penipu menggunakan alat open-source untuk menduplikasi website resmi dalam hitungan detik.",
                "Alat seperti Zphisher atau Shellphish mampu menghasilkan halaman login palsu untuk Facebook, Google, atau Bank hanya dalam waktu kurang dari 30 detik."
            ),
            tips = listOf(
                "Halaman palsu dibuat sangat cepat, jangan terpaku pada desain 'bagus'",
                "Selalu periksa ejaan URL (misal: bca.co.id vs bca-verifikasi.xyz)",
                "Gunakan pengelola kata sandi (Password Manager) yang hanya mengisi data di situs asli",
                "Laporkan website phishing ke ID-CERT atau penyedia layanan terkait"
            ),
            imageResId = R.drawable.phising
        )
    )

    fun findById(id: String?): EducationContent =
        all.find { it.id == id } ?: all.first()
}
