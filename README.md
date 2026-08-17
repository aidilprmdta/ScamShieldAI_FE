# 📱 ScamShield AI — Frontend (Android)

Aplikasi Android berbasis **Jetpack Compose** untuk platform deteksi dan edukasi anti-penipuan digital ScamShield AI.

---

## 📋 Prasyarat Sistem

- **Android Studio**: Android Studio Hedgehog / Iguana / Ladybug / Meerkat atau yang lebih baru (mendukung Android Gradle Plugin 8.x/9.x).
- **JDK**: Java Development Kit (JDK) 17 atau 21.
- **Android SDK**:
  - `minSdk`: **24** (Android 7.0 Nougat)
  - `targetSdk` / `compileSdk`: **36** (Android 15 / 16)
- **Perangkat Uji**:
  - Android Emulator (disarankan Google Play API level 30+) atau
  - Smartphone Android fisik dengan opsi *USB Debugging* aktif.

---

## ⚙️ 1. Pengaturan Firebase Android

Aplikasi memerlukan file konfigurasi `google-services.json` agar fitur Firebase Authentication, Firestore, dan FCM Push Notifications berfungsi:

### Langkah-langkah Setup Firebase Console:
1. Buka [Firebase Console](https://console.firebase.google.com/) dan pilih project Anda.
2. Tambahkan aplikasi Android (*Add App* > 🤖 *Android*):
   - **Android package name**: `com.example.scamshieldai` *(wajib persis)*
   - **App nickname**: `ScamShield AI`
3. **Tambahkan SHA-1 & SHA-256 Fingerprint** (Wajib untuk Google Sign-In):
   - Buka terminal di Android Studio atau PowerShell, lalu jalankan:
     ```bash
     # Di Windows:
     keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
     
     # Di macOS/Linux:
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     ```
   - Salin nilai **SHA-1** dan **SHA-256** ke kolom *Debug signing certificate SHA-1* pada Firebase Console.
4. **Unduh `google-services.json`**:
   - Download file `google-services.json`.
   - Pindahkan file tersebut ke folder:
     ```text
     ScamShieldAI_FE/app/google-services.json
     ```
5. **Konfigurasi Web Client ID**:
   - Di Firebase Console > *Authentication* > *Sign-in method* > *Google*, perhatikan *Web SDK configuration* (Web Client ID).
   - Build script Gradle akan otomatis membaca `client_id` (tipe 3) dari `google-services.json`.

---

## 🔧 2. Konfigurasi Backend URL (`local.properties`)

Aplikasi berkomunikasi dengan server backend FastAPI via REST API. Konfigurasikan alamat server pada file `local.properties`:

1. Buka file `ScamShieldAI_FE/local.properties` (buat jika belum ada).
2. Tentukan `DEV_BASE_URL` sesuai target pengujian Anda:

```properties
sdk.dir=C:\\Users\\<NAMA_USER>\\AppData\\Local\\Android\\Sdk

# Opsi A: Jika menggunakan Android Emulator di PC yang sama:
DEV_BASE_URL=http://10.0.2.2:8000/

# Opsi B: Jika menggunakan Smartphone Fisik (PC dan Smartphone terhubung di Wi-Fi yang sama):
# Cari IP PC Anda (misal: jalankan `ipconfig` di Windows -> IPv4: 192.168.1.50)
# DEV_BASE_URL=http://192.168.1.50:8000/
```

> [!IMPORTANT]
> - URL `DEV_BASE_URL` **wajib diakhiri dengan garis miring `/`**.
> - Pastikan server backend dijalankan dengan opsi `--host 0.0.0.0` agar dapat diakses dari luar localhost PC.

---

## 🚀 3. Build & Menjalankan Aplikasi

### Melalui Android Studio (Direkomendasikan):
1. Buka Android Studio.
2. Pilih menu **File > Open**, lalu arahkan ke folder `ScamShieldAI_FE`.
3. Tunggu proses **Gradle Sync** hingga selesai.
4. Pilih target device (Emulator atau USB Device).
5. Klik tombol **Run ▶** (atau tekan `Shift + F10`).

### Melalui Command Line (Gradle Wrapper):

```bash
cd ScamShieldAI_FE

# Build Debug APK:
# Windows:
.\gradlew.bat assembleDebug
# macOS/Linux:
./gradlew assembleDebug

# Install langsung ke device yang terhubung:
.\gradlew.bat installDebug
```

File APK hasil build akan berlokasi di:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📱 4. Izin Aplikasi (Permissions)

Aplikasi telah mengonfigurasi izin berikut di `AndroidManifest.xml`:
- `android.permission.INTERNET`: Untuk komunikasi REST API dengan server backend.
- `android.permission.CAMERA`: Untuk pemindaian langsung kode QR/QRIS via CameraX.
- `android.permission.POST_NOTIFICATIONS`: Untuk menerima pemberitahuan push status laporan verifikasi penipuan (Android 13+).
- `android.permission.ACCESS_NETWORK_STATE`: Untuk mendeteksi ketersediaan koneksi internet.

---

## 🔍 5. Troubleshooting & FAQ

| Kendala | Penyebab Umum | Solusi |
|---|---|---|
| **Koneksi gagal / Timeout ke server** | Alamat IP salah atau firewall memblokir port 8000. | - Periksa apakah backend aktif di `0.0.0.0:8000`.<br>- Jika pakai HP fisik, pastikan HP dan PC terhubung di Wi-Fi yang sama.<br>- Periksa Windows Firewall agar mengizinkan koneksi port 8000.<br>- Update `DEV_BASE_URL` di `local.properties` lalu rebuild. |
| **Google Sign-In Error (10 / Developer Error)** | SHA-1 keystore belum didaftarkan di Firebase. | Daftarkan SHA-1 debug keystore Anda di Firebase Console Project Settings, lalu download ulang `google-services.json`. |
| **Google Sign-In Canceled / No Credential** | Akun Google belum login di device/emulator. | Buka *Settings > Accounts* di Android device/emulator dan login akun Google Anda terlebih dahulu. |
| **Kamera QR tidak muncul** | Izin kamera belum diberikan. | Tekan tombol "Izinkan Kamera" di layar Scan QR atau aktifkan melalui *Pengaturan Aplikasi > Permissions > Camera*. Anda juga dapat memilih gambar QR langsung dari galeri. |
