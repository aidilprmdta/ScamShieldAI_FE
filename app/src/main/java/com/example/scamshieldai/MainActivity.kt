package com.example.scamshieldai

import android.content.Intent
import android.os.Bundle
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

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.auth.BiometricHelper
import com.example.scamshieldai.network.ScamShieldRepository
import com.example.scamshieldai.settings.AppPreferences
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    private val pendingNotificationRoute = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingNotificationRoute.value = parseNotificationRoute(intent)
        enableEdgeToEdge()
        setContent {
            val darkMode by AppPreferences.darkModeFlow(this).collectAsState(initial = false)
            val notifRoute = pendingNotificationRoute.value
            ScamShieldTheme(darkTheme = darkMode) {
                ScamShieldApp(
                    activity = this@MainActivity,
                    pendingNotificationRoute = notifRoute,
                    onNotificationRouteConsumed = { pendingNotificationRoute.value = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingNotificationRoute.value = parseNotificationRoute(intent)
    }

    private fun parseNotificationRoute(intent: Intent?): String? {
        val type = intent?.getStringExtra("type") ?: return null
        return when (type) {
            "report_status_updated" -> {
                val reportId = intent.getStringExtra("report_id") ?: return null
                "report_status/$reportId"
            }
            "new_report" -> "admin_reports"
            else -> null
        }
    }
}

@Composable
fun ScamShieldApp(
    activity: FragmentActivity? = null,
    pendingNotificationRoute: String? = null,
    onNotificationRouteConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()
    val repository = remember { ScamShieldRepository() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val token = AuthTokenStore.idToken
    
    val snackbarHostState = remember { SnackbarHostState() }
    var lastResult by remember { mutableStateOf<ScanResult?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var completedEducationIds by remember { mutableStateOf(setOf<String>()) }
    val historyList = remember { mutableStateListOf<HistoryItem>() }
    var isHistoryLoading by remember { mutableStateOf(false) }
    var userEmail by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    val adminReports = remember { mutableStateListOf<com.example.scamshieldai.network.AdminReportItem>() }
    var isAdminLoading by remember { mutableStateOf(false) }
    val myReports = remember { mutableStateListOf<com.example.scamshieldai.network.UserReportItem>() }
    var isMyReportsLoading by remember { mutableStateOf(false) }
    var reportDetail by remember { mutableStateOf<com.example.scamshieldai.network.UserReportItem?>(null) }
    var isReportDetailLoading by remember { mutableStateOf(false) }
    var pendingMyReportsCount by remember { mutableIntStateOf(0) }
    var pendingAdminReportsCount by remember { mutableIntStateOf(0) }

    fun showError(message: String) {
        coroutineScope.launch { snackbarHostState.showSnackbar(message) }
    }

    LaunchedEffect(Unit) {
        val biometricEnabled = AppPreferences.biometricEnabledFlow(context).first()
        val savedToken = AppPreferences.savedTokenFlow(context).first()
        val savedRefresh = AppPreferences.savedRefreshTokenFlow(context).first()
        if (biometricEnabled && savedToken != null && activity != null && BiometricHelper.canAuthenticate(activity)) {
            BiometricHelper.authenticate(
                activity = activity,
                onSuccess = {
                    AuthTokenStore.setToken(savedToken, savedRefresh)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onError = { /* fall through to login screen */ }
            )
        } else if (savedToken != null && !biometricEnabled) {
            AuthTokenStore.setToken(savedToken, savedRefresh)
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    fun mapApiResultToScanResult(result: com.example.scamshieldai.network.AnalysisResult): ScanResult {
        val level = when (result.riskLevel.lowercase()) {
            "high" -> RiskLevel.HIGH
            "medium" -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
        val flags = result.redFlags?.map { it.label } ?: emptyList()
        return ScanResult(
            type = result.type,
            riskScore = result.riskScore,
            riskLevel = level,
            inputSummary = result.inputSummary,
            flags = flags,
            explanation = result.explanation,
            recommendation = result.recommendationText ?: result.recommendation,
            relatedArticle = result.relatedEducationCategory
        )
    }

    val fetchHistory: () -> Unit = {
        coroutineScope.launch {
            isHistoryLoading = true
            repository.getHistory().onSuccess { response ->
                historyList.clear()
                for (item in response.data) {
                    val scanResult = mapApiResultToScanResult(item)
                    historyList.add(HistoryItem(item.scanId ?: "", scanResult, "Tersimpan"))
                }
            }.onFailure {
                historyList.clear()
                showError("Gagal memuat riwayat: ${it.message}")
            }
            isHistoryLoading = false
        }
    }

    val fetchReportBadgeCounts: () -> Unit = {
        coroutineScope.launch {
            if (token == null) {
                pendingMyReportsCount = 0
                pendingAdminReportsCount = 0
                return@launch
            }
            repository.getMyPendingReportCount().onSuccess { pendingMyReportsCount = it }
            if (isAdmin) {
                repository.getAdminPendingReportCount().onSuccess { pendingAdminReportsCount = it }
            } else {
                pendingAdminReportsCount = 0
            }
        }
    }

    val fetchMyReports: () -> Unit = {
        coroutineScope.launch {
            isMyReportsLoading = true
            repository.getMyReports().onSuccess { list ->
                myReports.clear()
                myReports.addAll(list)
            }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
            isMyReportsLoading = false
        }
    }

    LaunchedEffect(token) {
        fetchHistory()
        if (token != null) {
            repository.getMe().onSuccess { me ->
                userEmail = me.email ?: ""
                isAdmin = me.admin
                coroutineScope.launch {
                    repository.getMyPendingReportCount().onSuccess { pendingMyReportsCount = it }
                    if (me.admin) {
                        repository.getAdminPendingReportCount().onSuccess { pendingAdminReportsCount = it }
                    } else {
                        pendingAdminReportsCount = 0
                    }
                }
            }
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { fcmToken: String ->
                    coroutineScope.launch { repository.registerFcmToken(fcmToken) }
                }
        } else {
            userEmail = ""
            isAdmin = false
            pendingMyReportsCount = 0
            pendingAdminReportsCount = 0
        }
    }

    LaunchedEffect(pendingNotificationRoute, token, isAdmin) {
        val route = pendingNotificationRoute ?: return@LaunchedEffect
        when {
            route.startsWith("report_status/") -> {
                if (token == null) {
                    showError("Silakan login untuk melihat laporan.")
                } else {
                    val reportId = route.removePrefix("report_status/")
                    navController.navigate("report_status/$reportId")
                }
            }
            route == "admin_reports" -> {
                if (!isAdmin) {
                    showError("Akses ditolak: hanya admin.")
                } else {
                    coroutineScope.launch {
                        isAdminLoading = true
                        repository.getAdminReports().onSuccess { list ->
                            adminReports.clear()
                            adminReports.addAll(list)
                        }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
                        isAdminLoading = false
                    }
                    navController.navigate("admin_reports")
                }
            }
        }
        onNotificationRouteConsumed()
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

    val profileNavBadge = if (isAdmin) pendingAdminReportsCount else pendingMyReportsCount

    val navItems = remember(profileNavBadge) {
        listOf(
            NavigationItemData("Beranda", "home", Icons.Filled.Home, Icons.Outlined.Home),
            NavigationItemData("Riwayat", "history", Icons.Filled.History, Icons.Outlined.History),
            NavigationItemData("Edukasi", "education_center", Icons.Filled.School, Icons.Outlined.School),
            NavigationItemData("Profil", "profile", Icons.Filled.Person, Icons.Outlined.Person, badgeCount = profileNavBadge)
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: "home"

    // Logic for highlighting the correct tab even in sub-pages
    val selectedRoute = when {
        currentRoute.startsWith("education_detail") -> "education_center"
        else -> currentRoute
    }

    // Logic for showing Navbar: Hide on scanning, result, and auth screens
    val hideNavBarRoutes = listOf(
        "login", "register",
        "scan_chat", "check_link", "scan_screenshot", "scan_qr", 
        "analyzing", "result", "block_delete", "report", "quiz_screen",
        "education_detail", "security_privacy", "notifications", "about",
        "admin_reports", "my_reports", "report_status"
    )
    val showNavBar = !hideNavBarRoutes.any { currentRoute.startsWith(it) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("login") {
                    val loginContext = LocalContext.current
                    LoginScreen(
                        onLoginSuccess = {
                            coroutineScope.launch {
                                AppPreferences.setSavedToken(loginContext, AuthTokenStore.idToken, AuthTokenStore.refreshToken)
                            }
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate("register")
                        }
                    )
                }
                composable("register") {
                    val regContext = LocalContext.current
                    RegisterScreen(
                        onRegisterSuccess = {
                            coroutineScope.launch {
                                AppPreferences.setSavedToken(regContext, AuthTokenStore.idToken, AuthTokenStore.refreshToken)
                            }
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate("login")
                        }
                    )
                }
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
                            navController.navigate("education_center") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
                            navController.navigate("analyzing")
                            coroutineScope.launch {
                                repository.analyzeChat(text).onSuccess { apiResult ->
                                    lastResult = mapApiResultToScanResult(apiResult)
                                    fetchHistory()
                                }.onFailure {
                                    showError("Gagal menganalisis: ${it.message}")
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                }
                composable("check_link") {
                    CheckLinkScreen(
                        onBack = { navController.popBackStack() },
                        onCheck = { url ->
                            navController.navigate("analyzing")
                            coroutineScope.launch {
                                repository.analyzeLink(url).onSuccess { apiResult ->
                                    lastResult = mapApiResultToScanResult(apiResult)
                                    fetchHistory()
                                }.onFailure {
                                    showError("Gagal menganalisis link: ${it.message}")
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                }
                composable("scan_screenshot") {
                    ScanScreenshotScreen(
                        onBack = { navController.popBackStack() },
                        onAnalyzeText = { text ->
                            navController.navigate("analyzing")
                            coroutineScope.launch {
                                repository.analyzeChat(text, source = "screenshot_ocr").onSuccess { apiResult ->
                                    lastResult = mapApiResultToScanResult(apiResult)
                                    fetchHistory()
                                }.onFailure {
                                    showError("Gagal menganalisis screenshot: ${it.message}")
                                    navController.popBackStack()
                                }
                            }
                        },
                        onDemoSelected = {
                            val demoText = "Selamat! Anda terpilih mendapatkan hadiah Rp 25.000.000 dari BRI. Klaim sekarang di http://bri-hadiah.xyz/klaim sebelum 24 jam."
                            navController.navigate("analyzing")
                            coroutineScope.launch {
                                repository.analyzeChat(demoText, source = "screenshot_ocr").onSuccess { apiResult ->
                                    lastResult = mapApiResultToScanResult(apiResult)
                                    fetchHistory()
                                }.onFailure {
                                    showError("Gagal menganalisis: ${it.message}")
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                }
                composable("scan_qr") {
                    ScanQRScreen(
                        onBack = { navController.popBackStack() },
                        onScanned = { data ->
                            navController.navigate("analyzing")
                            coroutineScope.launch {
                                repository.analyzeQr(data).onSuccess { apiResult ->
                                    lastResult = mapApiResultToScanResult(apiResult)
                                    fetchHistory()
                                }.onFailure {
                                    showError("Gagal menganalisis QR: ${it.message}")
                                    navController.popBackStack()
                                }
                            }
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
                        isLoading = isHistoryLoading,
                        onBack = { navController.popBackStack() },
                        onItemClick = { item ->
                            lastResult = item.result
                            navController.navigate("result")
                        },
                        onDeleteItem = { item ->
                            coroutineScope.launch {
                                repository.deleteHistory(item.id)
                                    .onSuccess { fetchHistory() }
                                    .onFailure { showError("Gagal menghapus: ${it.message}") }
                            }
                        },
                        onRefresh = { fetchHistory() }
                    )
                }
                composable("profile") {
                    val context = LocalContext.current
                    val isDarkMode by AppPreferences.darkModeFlow(context).collectAsState(initial = false)
                    val isBiometricEnabled by AppPreferences.biometricEnabledFlow(context).collectAsState(initial = false)
                    LaunchedEffect(Unit) { fetchReportBadgeCounts() }
                    ProfileScreen(
                        userName = userEmail.substringBefore("@").ifEmpty { "Pengguna ScamShield" },
                        userEmail = userEmail,
                        scanCount = historyList.size,
                        threatCount = historyList.count { it.result.riskLevel == RiskLevel.HIGH },
                        isDarkMode = isDarkMode,
                        isBiometricEnabled = isBiometricEnabled,
                        isAdmin = isAdmin,
                        pendingMyReportsCount = pendingMyReportsCount,
                        pendingAdminReportsCount = pendingAdminReportsCount,
                        onDarkModeToggle = { enabled ->
                            coroutineScope.launch { AppPreferences.setDarkMode(context, enabled) }
                        },
                        onBiometricToggle = { enabled ->
                            coroutineScope.launch { AppPreferences.setBiometricEnabled(context, enabled) }
                        },
                        onAdminReportsClick = {
                            coroutineScope.launch {
                                isAdminLoading = true
                                repository.getAdminReports().onSuccess { list ->
                                    adminReports.clear()
                                    adminReports.addAll(list)
                                }.onFailure { showError("Gagal memuat laporan: ${it.message}") }
                                isAdminLoading = false
                            }
                            navController.navigate("admin_reports")
                        },
                        onMyReportsClick = {
                            fetchMyReports()
                            navController.navigate("my_reports")
                        },
                        onSecurityClick = {
                            navController.navigate("security_privacy")
                        },
                        onNotificationClick = {
                            navController.navigate("notifications")
                        },
                        onAboutClick = {
                            navController.navigate("about")
                        },
                        onLogout = {
                            AuthTokenStore.clear()
                            historyList.clear()
                            coroutineScope.launch { AppPreferences.setSavedToken(context, null) }
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
                composable("my_reports") {
                    MyReportsScreen(
                        reports = myReports,
                        isLoading = isMyReportsLoading,
                        onBack = { navController.popBackStack() },
                        onReportClick = { report ->
                            navController.navigate("report_status/${report.reportId}")
                        }
                    )
                }
                composable(
                    route = "report_status/{report_id}",
                    arguments = listOf(navArgument("report_id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val reportId = backStackEntry.arguments?.getString("report_id") ?: ""
                    LaunchedEffect(reportId) {
                        if (reportId.isNotEmpty()) {
                            isReportDetailLoading = true
                            reportDetail = null
                            repository.getMyReport(reportId).onSuccess { reportDetail = it }
                                .onFailure {
                                    showError("Gagal memuat detail: ${it.message}")
                                    reportDetail = null
                                }
                            isReportDetailLoading = false
                        }
                    }
                    ReportStatusScreen(
                        report = if (reportDetail?.reportId == reportId) reportDetail else null,
                        isLoading = isReportDetailLoading,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("admin_reports") {
                    if (!isAdmin) {
                        LaunchedEffect(Unit) {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                            }
                            showError("Akses ditolak: hanya admin yang bisa mengelola laporan.")
                        }
                    } else {
                        AdminReportsScreen(
                            reports = adminReports,
                            isLoading = isAdminLoading,
                            onBack = { navController.popBackStack() },
                            onVerify = { reportId ->
                                coroutineScope.launch {
                                    repository.updateReportStatus(reportId, "verified").onSuccess { result ->
                                        adminReports.replaceAll {
                                            if (it.reportId == reportId) it.copy(
                                                verifiedStatus = "verified",
                                                verifiedBy = result.verifiedBy,
                                                verifiedByEmail = result.verifiedByEmail,
                                                verifiedAt = result.verifiedAt
                                            ) else it
                                        }
                                        fetchReportBadgeCounts()
                                    }.onFailure { showError("Gagal verifikasi: ${it.message}") }
                                }
                            },
                            onReject = { reportId ->
                                coroutineScope.launch {
                                    repository.updateReportStatus(reportId, "rejected").onSuccess { result ->
                                        adminReports.replaceAll {
                                            if (it.reportId == reportId) it.copy(
                                                verifiedStatus = "rejected",
                                                verifiedBy = result.verifiedBy,
                                                verifiedByEmail = result.verifiedByEmail,
                                                verifiedAt = result.verifiedAt
                                            ) else it
                                        }
                                        fetchReportBadgeCounts()
                                    }.onFailure { showError("Gagal menolak: ${it.message}") }
                                }
                            }
                        )
                    }
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
                        },
                        onSubmitReport = { type, content, note ->
                            coroutineScope.launch {
                                repository.submitReport(type, content, note).onSuccess {
                                    fetchReportBadgeCounts()
                                }
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
                        type = "unknown",
                        riskScore = 0,
                        riskLevel = RiskLevel.LOW,
                        inputSummary = "",
                        flags = emptyList<String>(),
                        explanation = "Tidak ada hasil analisis.",
                        recommendation = "Silakan lakukan scan terlebih dahulu."
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
