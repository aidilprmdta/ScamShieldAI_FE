# Rencana Perbaikan Layout Ubah Kata Sandi

Rencana ini bertujuan untuk menyelaraskan desain halaman `ChangePasswordScreen` dengan tema dan skema warna aplikasi (terutama mengikuti gaya `LoginScreen` yang lebih modern dan premium).

## Perubahan yang Diusulkan

### UI/UX & Desain

#### [UBAH] [ChangePasswordScreen.kt](file:///C:/Users/Martrio/Documents/Project-Lomba/Lomba-App/lomba'/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/ChangePasswordScreen.kt)
- **Gaya Header "Hero"**: Mengubah header menjadi gaya Hero dengan latar belakang `YaleBlue` dan dekorasi lingkaran kanvas, konsisten dengan halaman Login.
- **Surface Bulat**: Menggunakan `Surface` dengan sudut membulat (`topStart` & `topEnd` 40.dp) untuk area formulir.
- **Konsistensi Input**: Memperbarui `OutlinedTextField` agar menggunakan skema warna `YaleBlue` (fokus) dan `Slate100` (tidak fokus), serta menambahkan ikon di bagian depan (leading icon) seperti `Icons.Outlined.Lock`.
- **Animasikan Tombol**: Menggunakan `animateColorAsState` untuk transisi warna tombol submit berdasarkan validitas formulir.
- **Pesan Kesalahan**: Mengintegrasikan `SnackbarHost` untuk menampilkan pesan kesalahan yang lebih elegan.
- **Keyboard Handling**: Menambahkan `imePadding()` agar formulir tetap terlihat saat keyboard muncul.

## Rencana Verifikasi

### Pengujian Otomatis
- Menjalankan build Gradle untuk memastikan tidak ada kesalahan sintaks.

### Verifikasi Manual
- Membuka halaman "Ubah Kata Sandi" dari menu Profil/Keamanan.
- Memastikan tata letak terlihat rapi dan warna sesuai dengan bagian aplikasi lainnya.
- Mencoba mengubah kata sandi untuk memastikan validasi (minimal 6 karakter, konfirmasi cocok) tetap berjalan dengan benar.
