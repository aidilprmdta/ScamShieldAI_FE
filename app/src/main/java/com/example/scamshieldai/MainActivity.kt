package com.example.scamshieldai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scamshieldai.model.EducationContent
import com.example.scamshieldai.ui.components.AnalyzingOverlay
import com.example.scamshieldai.ui.components.FloatingNavBar
import com.example.scamshieldai.ui.components.NavigationItemData
import com.example.scamshieldai.ui.screens.*
import com.example.scamshieldai.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamShieldTheme {
                ScamShieldApp()
            }
        }
    }
}

@Composable
fun ScamShieldApp() {
    val navController = rememberNavController()
    var lastResult by remember { mutableStateOf<ScanResult?>(null) }
    
    // Track completed education item IDs
    var completedEducationIds by remember { mutableStateOf(setOf<String>()) }
    
    // Shared history state for the session
    val historyList = remember {
        mutableStateListOf(
            HistoryItem("1", ScanResult("link", 94, RiskLevel.HIGH, "http://bca-verified-promo.xyz/hadiah", emptyList(), "", ""), "1 jam lalu"),
            HistoryItem("2", ScanResult("chat", 88, RiskLevel.HIGH, "Selamat! Anda terpilih mendapatkan ha...", emptyList(), "", ""), "1 hari lalu"),
            HistoryItem("3", ScanResult("qr", 22, RiskLevel.LOW, "QR Code — tokopedia.com/promo/specia...", emptyList(), "", ""), "1 hari lalu"),
            HistoryItem("4", ScanResult("screenshot", 76, RiskLevel.MEDIUM, "Screenshot: \"Mama, ini nomor baru. Tran...", emptyList(), "", ""), "3 hari lalu")
        )
    }

    val educationContents = remember {
        listOf(
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
                id = "0", // Featured
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
    }

    val navItems = listOf(
        NavigationItemData("Beranda", "home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItemData("Riwayat", "history", Icons.Filled.History, Icons.Outlined.History),
        NavigationItemData("Edukasi", "education_center", Icons.Filled.School, Icons.Outlined.School),
        NavigationItemData("Profil", "profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: "home"

    // Logic for highlighting the correct tab even in sub-pages
    val selectedRoute = when {
        currentRoute.startsWith("education_detail") -> "education_center"
        else -> currentRoute
    }

    // Logic for showing Navbar: Hide on scanning, result, and education detail screens
    val hideNavBarRoutes = listOf(
        "scan_chat", "check_link", "scan_screenshot", "scan_qr", 
        "analyzing", "result", "block_delete", "report", "quiz_screen",
        "education_detail", "security_privacy", "notifications", "about"
    )
    val showNavBar = !hideNavBarRoutes.any { currentRoute.startsWith(it) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiteBackground
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("home") {
                    val threatCount = remember(historyList) {
                        historyList.count { it.result.riskLevel == RiskLevel.HIGH }
                    }
                    HomeScreen(
                        threatCount = threatCount,
                        onScanModeSelected = { mode ->
                            when (mode) {
                                "chat" -> navController.navigate("scan_chat")
                                "link" -> navController.navigate("check_link")
                                "screenshot" -> navController.navigate("scan_screenshot")
                                "qr" -> navController.navigate("scan_qr")
                                else -> navController.navigate("analyzing")
                            }
                        },
                        onEducationSelected = {
                            navController.navigate("education_center")
                        },
                        onHistoryClick = {
                            navController.navigate("history")
                        }
                    )
                }
                composable("scan_chat") {
                    ScanChatScreen(
                        onBack = { navController.popBackStack() },
                        onAnalyze = { text ->
                            val result = ScanResult(
                                type = "chat",
                                riskScore = 88,
                                riskLevel = RiskLevel.HIGH,
                                inputSummary = text,
                                flags = listOf("Janji hadiah mencurigakan", "Tekanan waktu/urgensi", "Tautan tidak resmi", "Permintaan tindakan segera"),
                                explanation = "Pesan ini mengandung beberapa tanda penipuan klasik: janji hadiah uang dalam jumlah besar, tekanan waktu (urgensi), dan tautan mencurigakan yang meniru domain resmi.",
                                recommendation = "Jangan klik tautan apapun. Blokir nomor/akun pengirim. Laporkan ke platform terkait.",
                                relatedArticle = "Waspada Phishing: Kenali Tautan Palsu"
                            )
                            lastResult = result
                            historyList.add(0, HistoryItem(System.currentTimeMillis().toString(), result, "Baru saja"))
                            navController.navigate("analyzing")
                        }
                    )
                }
                composable("check_link") {
                    CheckLinkScreen(
                        onBack = { navController.popBackStack() },
                        onCheck = { url ->
                            val isScam = url.contains("promo") || url.contains("bri") || url.contains("bit.ly") || url.contains(".xyz")
                            val result = ScanResult(
                                type = "link",
                                riskScore = if (isScam) 81 else 12,
                                riskLevel = if (isScam) RiskLevel.HIGH else RiskLevel.LOW,
                                inputSummary = url,
                                flags = if (isScam) listOf(
                                    "Domain tidak resmi (.xyz)",
                                    "Meniru merek keuangan",
                                    "Kata kunci phishing",
                                    "Tidak ada HTTPS valid"
                                ) else emptyList<String>(),
                                explanation = if (isScam) "Tautan ini mengarah ke domain tidak resmi yang meniru merek perbankan/e-commerce Indonesia. Domain .xyz tidak digunakan oleh lembaga keuangan resmi. Pola URL menggunakan kata kunci \"hadiah\" dan \"verifikasi\" yang umum pada phishing." else "Tautan ini mengarah ke domain resmi yang terverifikasi dan aman untuk dikunjungi.",
                                recommendation = if (isScam) "Jangan buka tautan ini. Hapus pesan yang berisi tautan. Laporkan ke pihak yang namanya digunakan." else "Tautan ini aman. Namun, tetap pastikan Anda berada di halaman yang benar sebelum memasukkan data sensitif.",
                                relatedArticle = if (isScam) "Waspada Phishing: Kenali Tautan Palsu" else null
                            )
                            lastResult = result
                            historyList.add(0, HistoryItem(System.currentTimeMillis().toString(), result, "Baru saja"))
                            navController.navigate("analyzing")
                        }
                    )
                }
                composable("scan_screenshot") {
                    ScanScreenshotScreen(
                        onBack = { navController.popBackStack() },
                        onImageSelected = { uri ->
                            val result = ScanResult(
                                type = "screenshot",
                                riskScore = 91,
                                riskLevel = RiskLevel.HIGH,
                                inputSummary = "Screenshot: \"Selamat! Anda terpilih mendapatkan hadiah Rp 25.000.000...\"",
                                flags = listOf("Janji hadiah uang", "Domain palsu (.xyz)", "Urgensi waktu 24 jam", "Bukan kanal resmi BRI"),
                                explanation = "Screenshot ini mengandung teks dengan indikator penipuan tinggi: janji hadiah uang dari lembaga keuangan, batas waktu klaim yang sangat pendek (24 jam), dan tautan ke domain tidak resmi (.xyz) yang meniru BRI.",
                                recommendation = "Ini hampir pasti penipuan. Abaikan dan hapus pesan. Jangan klik tautan. Laporkan ke BRI melalui 14017 atau halo.bri.co.id.",
                                relatedArticle = "Waspada Phishing: Kenali Tautan Palsu"
                            )
                            lastResult = result
                            historyList.add(0, HistoryItem(System.currentTimeMillis().toString(), result, "Baru saja"))
                            navController.navigate("analyzing")
                        },
                        onDemoSelected = {
                            val result = ScanResult(
                                type = "screenshot",
                                riskScore = 91,
                                riskLevel = RiskLevel.HIGH,
                                inputSummary = "Screenshot Demo: \"Selamat! Anda terpilih mendapatkan hadiah Rp 25.000.000...\"",
                                flags = listOf("Janji hadiah uang", "Domain palsu (.xyz)", "Urgensi waktu 24 jam", "Bukan kanal resmi BRI"),
                                explanation = "Screenshot ini mengandung teks dengan indikator penipuan tinggi: janji hadiah uang dari lembaga keuangan, batas waktu klaim yang sangat pendek (24 jam), dan tautan ke domain tidak resmi (.xyz) yang meniru BRI.",
                                recommendation = "Ini hampir pasti penipuan. Abaikan dan hapus pesan. Jangan klik tautan. Laporkan ke BRI melalui 14017 atau halo.bri.co.id.",
                                relatedArticle = "Waspada Phishing: Kenali Tautan Palsu"
                            )
                            lastResult = result
                            historyList.add(0, HistoryItem(System.currentTimeMillis().toString(), result, "Baru saja"))
                            navController.navigate("analyzing")
                        }
                    )
                }
                composable("scan_qr") {
                    ScanQRScreen(
                        onBack = { navController.popBackStack() },
                        onScanned = { data ->
                            val result = ScanResult(
                                type = "qr",
                                riskScore = 94,
                                riskLevel = RiskLevel.HIGH,
                                inputSummary = "QR Code: $data",
                                flags = listOf("Tautan eksternal tidak aman", "Mengarahkan ke form data diri", "Domain mencurigakan"),
                                explanation = "QR Code ini berisi tautan yang mengarah ke formulir pengumpulan data pribadi ilegal. Modus ini sering digunakan untuk mencuri kredensial akun.",
                                recommendation = "Jangan pernah memasukkan data KTP atau nomor rekening pada halaman yang dibuka dari QR ini. Laporkan jika diminta melakukan pembayaran.",
                                relatedArticle = "Waspada QR Phishing (Quishing)"
                            )
                            lastResult = result
                            historyList.add(0, HistoryItem(System.currentTimeMillis().toString(), result, "Baru saja"))
                            navController.navigate("analyzing")
                        }
                    )
                }
                composable(
                    route = "education_detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val content = educationContents.find { it.id == id } ?: educationContents[0]
                    EducationDetailScreen(
                        content = content,
                        onBack = {
                            id?.let { completedEducationIds = completedEducationIds + it }
                            navController.popBackStack()
                        }
                    )
                }
                composable("education_center") {
                    EducationCenterScreen(
                        completedIds = completedEducationIds,
                        onBack = { navController.popBackStack() },
                        onItemClick = { item ->
                            if (item.type == "KUIS") {
                                navController.navigate("quiz_screen")
                            } else {
                                navController.navigate("education_detail/${item.id}")
                            }
                        }
                    )
                }
                composable("quiz_screen") {
                    QuizScreen(
                        onBack = { navController.popBackStack() },
                        onFinish = {
                            completedEducationIds = completedEducationIds + "4" // ID for Quiz
                            navController.popBackStack()
                        }
                    )
                }
                composable("history") {
                    HistoryScreen(
                        historyList = historyList,
                        onBack = { navController.popBackStack() },
                        onItemClick = { item ->
                            lastResult = item.result
                            navController.navigate("result")
                        }
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        onSecurityClick = {
                            navController.navigate("security_privacy")
                        },
                        onNotificationClick = {
                            navController.navigate("notifications")
                        },
                        onAboutClick = {
                            navController.navigate("about")
                        }
                    )
                }
                composable("security_privacy") {
                    SecurityPrivacyScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("notifications") {
                    NotificationScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("about") {
                    AboutScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("block_delete") {
                    BlockDeleteScreen(
                        onBackToHome = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
                composable("report") {
                    ReportScreen(
                        onBack = { navController.popBackStack() },
                        onSubmit = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
                composable("analyzing") {
                    AnalyzingOverlay(
                        onAnalysisComplete = {
                            navController.navigate("result") {
                                popUpTo("analyzing") { inclusive = true }
                            }
                        }
                    )
                }
                composable("result") {
                    val result = lastResult ?: ScanResult(
                        type = "demo",
                        riskScore = 0,
                        riskLevel = RiskLevel.LOW,
                        inputSummary = "Demo text",
                        flags = emptyList<String>(),
                        explanation = "Demo explanation",
                        recommendation = "Demo recommendation"
                    )
                    ResultScreen(
                        result = result,
                        onBackToHome = {
                            navController.navigate("home") {
                                popUpTo("result") { inclusive = true }
                            }
                        },
                        onHistoryClick = {
                            navController.navigate("history")
                        },
                        onBlockDeleteClick = {
                            navController.navigate("block_delete")
                        },
                        onReportClick = {
                            navController.navigate("report")
                        }
                    )
                }
            }

            // Floating Navigation Bar
            if (showNavBar) {
                FloatingNavBar(
                    items = navItems,
                    currentRoute = selectedRoute,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
