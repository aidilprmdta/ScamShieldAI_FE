# Perbaikan Layout Ubah Kata Sandi Berhasil

Saya telah memperbarui halaman `ChangePasswordScreen` agar memiliki desain yang konsisten dengan tema utama aplikasi (terutama gaya premium pada halaman Login).

## Perubahan Utama

### 1. Desain Header "Hero"
- Menambahkan latar belakang `YaleBlue` yang elegan dengan dekorasi lingkaran halus menggunakan `Canvas`.
- Menampilkan teks judul yang lebih besar dan deskripsi yang lebih jelas untuk memberikan konteks keamanan.

### 2. Form Input Modern
- Memindahkan area formulir ke dalam `Surface` putih dengan sudut membulat 40dp.
- Menambahkan ikon `Lock` pada setiap kolom input kata sandi.
- Menggunakan skema warna `YaleBlue` untuk border saat kolom sedang difokuskan, konsisten dengan elemen UI lainnya.

### 3. Peningkatan UX (User Experience)
- **Animasi Tombol**: Tombol "Perbarui Kata Sandi" kini memiliki transisi warna yang halus (biru saat valid, abu-abu saat belum valid).
- **Penanganan Keyboard**: Menambahkan `imePadding` sehingga formulir tidak akan tertutup saat pengguna sedang mengetik.
- **Validasi Visual**: Pesan kesalahan muncul secara otomatis di bawah kolom input jika kriteria tidak terpenuhi (misalnya: kata sandi tidak cocok).

## Hasil Verifikasi
- **Gradle Build**: Berhasil dijalankan tanpa error.
- **Visual Check**: Layout kini sinkron dengan halaman `LoginScreen` dan `EditProfileScreen`.

render_diffs(file:///C:/Users/Martrio/Documents/Project-Lomba/Lomba-App/lomba'/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/ChangePasswordScreen.kt)

> [!TIP]
> Desain baru ini memberikan kesan yang lebih aman dan profesional, sangat cocok untuk fitur yang berkaitan dengan privasi pengguna.
