# Redesign Halaman Login & Register

Mendesain ulang halaman Login dan Register sesuai dengan referensi visual yang diberikan, menggunakan layout "Hero + Card" yang modern dan bersih.

## Proposed Changes

### Screens

#### [MODIFY] [LoginScreen.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/LoginScreen.kt)
- **Layout Structure**: Menggunakan `Box` sebagai root untuk menumpuk kartu putih di atas background hero.
- **Hero Section**:
    - Background: `YaleBlue`.
    - Dekorasi: Menambahkan bentuk organik/blob transparan untuk estetika.
    - Teks: "Hello!" besar dan sub-teks "Welcome back to ScamShield AI".
- **Form Card**:
    - Background: `White`.
    - Shape: `RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)`.
    - Fields: Email dan Password dengan gaya minimalist.
    - Social Login: Menambahkan deretan ikon (Facebook, Google, Apple) di bagian bawah form.

#### [MODIFY] [RegisterScreen.kt](file:///C:/Users/Martrio/AndroidStudioProjects/ScamShieldAI/app/src/main/java/com/example/scamshieldai/ui/screens/RegisterScreen.kt)
- **Layout Structure**: Menyamakan dengan halaman Login (Hero + Card).
- **Header Card**: Menambahkan tombol "← Back to login" di bagian atas kartu sesuai referensi.
- **Fields**: Nama, Email, Password, dan Nomor Telepon.
- **Design**: Menggunakan skema warna yang konsisten dengan identitas aplikasi.

## Visual Details
- **Warna Utama**: `YaleBlue` untuk header, `Cerulean` untuk aksen/tombol.
- **Corner Radius**: Menggunakan radius besar (`28.dp` - `40.dp`) untuk elemen utama agar terlihat lebih "friendly" dan modern.
- **Shadows**: Menambahkan bayangan halus pada kartu dan tombol.

## Verification Plan

### Manual Verification
- Membuka halaman Login: pastikan header biru dan kartu putih tampil proporsional.
- Membuka halaman Register: pastikan transisi dan tata letak konsisten dengan Login.
- Memastikan seluruh input field dan tombol dapat diinteraksi dengan baik.
- Memeriksa keterbacaan teks di atas background `YaleBlue`.
