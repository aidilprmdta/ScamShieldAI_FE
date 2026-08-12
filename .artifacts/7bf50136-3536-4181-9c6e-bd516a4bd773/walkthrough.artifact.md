# Walkthrough: Fix CLEARTEXT Network Security Error

Saya telah memperbaiki masalah koneksi jaringan yang memblokir proses registrasi Anda saat menggunakan server lokal.

## Perubahan yang Dilakukan

### 1. Konfigurasi Keamanan Jaringan ([network_security_config.xml](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/res/xml/network_security_config.xml))
- Menambahkan alamat IP server Anda (`10.12.69.218`) ke dalam daftar domain yang diizinkan untuk berkomunikasi menggunakan protokol **HTTP** (cleartext).
- Sebelumnya, Android memblokir koneksi ini karena secara default hanya mengizinkan HTTPS untuk keamanan.

## Hasil
- Aplikasi sekarang memiliki izin resmi dari sistem Android untuk mengirim data ke server di alamat `10.12.69.218`.
- Pesan error "CLEARTEXT communication not permitted" tidak akan muncul lagi saat Anda menekan tombol Sign Up.

## Verifikasi
- Proses build berhasil dikompilasi sepenuhnya.
- Konfigurasi XML sudah sesuai dengan standar Network Security Policy Android.

> [!TIP]
> Jika IP server Anda berubah lagi di masa mendatang, Anda hanya perlu menambahkan IP baru tersebut ke dalam file `network_security_config.xml` dengan cara yang sama.
