package com.example.scamshieldai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scamshieldai.model.EducationContent
import com.example.scamshieldai.ui.components.AnalyzingOverlay
import com.example.scamshieldai.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamShieldApp()
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
                )
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
                )
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
                )
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
                )
            )
        )
    }

    val navItems = listOf(
        NavigationItem("Beranda", "home", Icons.Default.Home),
        NavigationItem("Riwayat", "history", Icons.Default.History),
        NavigationItem("Edukasi", "education_center", Icons.Default.School),
        NavigationItem("Profil", "profile", Icons.Default.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: "home"

    // Show navbar only on main tabs
    val showNavBar = navItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showNavBar) {
                NavigationBar(
                    containerColor = Color(0xFF0B1628),
                    tonalElevation = 8.dp
                ) {
                    navItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title, fontSize = 10.sp) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2DD4BF),
                                selectedTextColor = Color(0xFF2DD4BF),
                                indicatorColor = Color(0xFF2DD4BF).copy(alpha = 0.1f),
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
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
                ProfileScreen()
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
    }
}

data class NavigationItem(val title: String, val route: String, val icon: ImageVector)
