# Rencana Perbaikan Bug Navbar & Restorasi Desain Beranda

Memperbaiki masalah Navbar yang tidak merespons klik (unresponsive) setelah navigasi dan mengembalikan desain Beranda ke versi lama namun tetap terhubung ke Riwayat Deteksi.

## Analisis Bug

> [!CAUTION]
> Masalah "unresponsive" pada Navbar kemungkinan besar disebabkan oleh tumpang tindih dengan **system gesture bar** di bagian bawah layar. Karena `navigationBarsPadding()` dihapus sebelumnya, Navbar berada terlalu rendah sehingga sistem Android mengambil alih input sentuhan untuk navigasi gestur (home/back).

## Proposed Changes

### UI Components

#### [MODIFY] [FloatingNavBar.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/components/FloatingNavBar.kt)
- Menambahkan kembali **`navigationBarsPadding()`** agar Navbar selalu berada di atas area navigasi sistem.
- Menambahkan **`zIndex(1f)`** untuk memastikan Navbar berada di lapisan teratas di atas konten `NavHost`.
- Menyesuaikan padding vertikal agar posisi terlihat proporsional dan mudah diklik.

### Core App Logic

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/MainActivity.kt)
- Memastikan rute `"education_center"` menampilkan Navbar dengan benar.
- Memastikan tombol lonceng di `HomeScreen` memanggil `onHistoryClick` yang mengarah ke rute `"history"`.
- Memperkuat logika penentuan `selectedRoute` agar Navbar selalu sinkron dengan halaman yang aktif.

### Screens

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/HomeScreen.kt)
- Memastikan penggunaan desain lama: Background gradient (`DeepNavy` ke `YaleBlue`) pada `HeroSection`.
- Memastikan tombol lonceng (notifikasi) terhubung ke fungsi `onHistoryClick`.

## Verification Plan

### Manual Verification
- Menjalankan aplikasi dan menekan banner "Edukasi".
- Memastikan di halaman "Pusat Edukasi", tombol "Beranda" pada Navbar dapat diklik kembali.
- Memastikan tombol lonceng di Beranda membuka halaman "Riwayat Deteksi".
- Memastikan Navbar tidak terpotong atau tertutup oleh bilah navigasi sistem.
