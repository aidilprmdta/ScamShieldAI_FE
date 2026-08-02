# Rencana Implementasi - Bottom Navigation Bar ScamShield AI (Update: Profil)

Rencana ini merinci penambahan bilah navigasi bawah (*Bottom Navigation Bar*) untuk mempermudah akses antar fitur utama aplikasi: Beranda, Riwayat, Edukasi, dan Profil.

## Komponen Utama UI

1. **NavigationBar (Material 3)**: Bilah navigasi modern di bagian bawah layar.
2. **Item Navigasi**:
    - **Beranda**: Menggunakan ikon `Home`.
    - **Riwayat**: Menggunakan ikon `History`.
    - **Edukasi**: Menggunakan ikon `School` (Pusat Edukasi).
    - **Profil**: Menggunakan ikon `Person` (Pengaturan Akun).
3. **Logika Visibilitas**: Navbar hanya akan muncul pada empat layar utama tersebut dan akan disembunyikan pada layar fungsional lainnya (seperti saat analisis AI atau input data) untuk menjaga fokus pengguna.

## Strategi Teknis

- **State Tracking**: Melacak rute aktif menggunakan `navController.currentBackStackEntryAsState()` untuk menyoroti item yang sedang dipilih.
- **Scaffold Integration**: Membungkus `NavHost` di dalam `Scaffold` dan menempatkan `NavigationBar` di parameter `bottomBar`.
- **Navigation Options**: Menggunakan `launchSingleTop = true` dan `restoreState = true` saat berpindah tab agar performa aplikasi tetap optimal.

## Perubahan yang Diusulkan

### UI Screens

#### [NEW] [ProfileScreen.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/ProfileScreen.kt)
- Membuat layar profil (saat ini sebagai placeholder) yang menampilkan informasi pengguna dan pengaturan dasar.

### Integrasi Utama

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/MainActivity.kt)
- Mendefinisikan struktur item navigasi bawah (Home, History, Education, Profile).
- Implementasi `Scaffold` dengan `NavigationBar`.
- Menyesuaikan rute navigasi untuk menyertakan layar Profil.

## Rencana Verifikasi

1. **Navigasi Tab**: Memastikan perpindahan antar empat menu utama berjalan lancar.
2. **Indikator Aktif**: Memastikan ikon di navbar menyoroti tab yang benar.
3. **Visibilitas**: Memastikan navbar menghilang pada alur proses (Scan/Analyzing/Result).
