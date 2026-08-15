# Redesain Tata Letak Halaman Beranda - Update Edukasi

Mengganti kartu "Welcome" pada halaman Beranda dengan kartu "Edukasi" sambil tetap mempertahankan estetika desain baru.

## Tinjauan Pengguna Diperlukan

> [!IMPORTANT]
> Kartu sambutan akan dihapus sepenuhnya dan digantikan oleh akses cepat ke Pusat Edukasi.

## Perubahan yang Diusulkan

### [Komponen UI]

#### [UBAH] [HomeScreen.kt](file:///C:/Users/Martrio/Documents/Project-Lomba/Lomba-App/lomba/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/HomeScreen.kt)
- Mengubah fungsi `WelcomeBanner` menjadi `EducationBannerCard`.
- Memperbarui konten teks menjadi "Pusat Edukasi" dan deskripsi mengenai artikel/kuis.
- Menggunakan aset gambar `R.drawable.edukasi`.
- Memastikan klik pada kartu mengarah ke `onEducationSelected`.

## Rencana Verifikasi

### Verifikasi Manual
- Pastikan kartu Edukasi muncul di bawah sapaan.
- Klik kartu tersebut dan pastikan diarahkan ke halaman Pusat Edukasi.
