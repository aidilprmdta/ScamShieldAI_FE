# Rencana Redesain Header Halaman Hasil Analisis (Hero Dynamic Style)

Rencana ini bertujuan untuk memperbarui tampilan header pada halaman Hasil Analisis (`ResultScreen`) menggunakan gaya "Hero" yang melengkung, namun tetap mempertahankan identitas warna berdasarkan tingkat risiko (3 warna).

## Perubahan yang Diusulkan

### [UBAH] [ResultScreen.kt](file:///C:/Users/Martrio/Documents/Project-Lomba/Lomba-App/lomba'/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/ResultScreen.kt)

#### 1. Header Hero dengan Warna Dinamis
- **Warna Latar**: Header akan menggunakan warna utama berdasarkan kategori risiko:
    - **Risiko Tinggi**: Menggunakan warna merah (`DangerRed`).
    - **Risiko Sedang**: Menggunakan warna kuning/oranye (`WarningYellow`).
    - **Aman**: Menggunakan warna hijau (`SafeGreen`) atau biru aman (`Cerulean`).
- **Layout Hero**:
    - Area header yang lebih luas di bagian atas.
    - Sudut bawah melengkung besar (**32dp - 40dp**) untuk transisi ke area konten.
    - Dekorasi sirkular transparan (Hero Style) untuk memberikan tekstur premium.
- **Teks Header**: Judul "Hasil Analisis" dan tipe scan akan menggunakan warna putih agar tetap terbaca jelas di atas warna-warna risiko tersebut.

#### 2. Kontainer Putih Melengkung
- Konten detail analisis akan berada di dalam kontainer putih yang menyambung dengan lengkungan header.
- Menjaga fokus pengguna pada skor `RiskGauge` yang berada di bagian atas kontainer putih.

#### 3. Konsistensi Navigasi
- Tombol kembali dan tombol riwayat akan menggunakan warna putih agar kontras dengan latar belakang header dinamis.

## Rencana Verifikasi

### Pengujian Otomatis
- Menjalankan build Gradle untuk memastikan tidak ada kesalahan sintaks.

### Verifikasi Manual
- Mencoba hasil scan dengan berbagai tingkat risiko (Tinggi, Sedang, Aman).
- Memastikan warna header berubah secara otomatis sesuai kategori hasil.
- Memastikan teks putih pada header memiliki keterbacaan yang baik di ketiga variasi warna (Merah, Kuning, Hijau).
