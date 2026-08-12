# Fix CLEARTEXT Network Security Error

Pesan error "CLEARTEXT communication not permitted" muncul karena Android secara default melarang pengiriman data melalui protokol HTTP (tanpa enkripsi) untuk alasan keamanan. Karena aplikasi Anda mencoba menghubungi server backend di IP lokal `10.12.69.218` menggunakan HTTP, Android memblokir koneksi tersebut.

## Proposed Changes

### Network Configuration

#### [MODIFY] [network_security_config.xml](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/res/xml/network_security_config.xml)
- Menambahkan alamat IP `10.12.69.218` ke dalam daftar domain yang diizinkan untuk menggunakan lalu lintas data *cleartext* (HTTP).

## Verification Plan

### Manual Verification
- Jalankan ulang aplikasi (`Rebuild Project`).
- Coba lakukan proses Registrasi kembali.
- Pastikan pesan error merah di bagian bawah layar tidak muncul lagi dan aplikasi dapat terhubung ke server.

> [!NOTE]
> Perubahan ini hanya untuk lingkungan pengembangan (development). Jika nanti aplikasi dirilis ke publik, sangat disarankan menggunakan HTTPS (SSL).
