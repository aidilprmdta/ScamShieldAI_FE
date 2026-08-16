# ScamShield AI — Frontend (Android)

App Jetpack Compose. Butuh backend di repo `scamshield_backend`.

## Prasyarat

- Android Studio (AGP 9.x), JDK 11+
- minSdk 24 / targetSdk 36
- `app/google-services.json` dari Firebase Console
- Backend sudah jalan

## Install & config

```bash
cd ScamShieldAI_FE
```

Edit `local.properties`:

```properties
sdk.dir=C:\\Users\\<USER>\\AppData\\Local\\Android\\Sdk

# Emulator:
DEV_BASE_URL=http://10.0.2.2:8000/

# HP fisik (satu Wi‑Fi dengan PC):
# DEV_BASE_URL=http://192.168.x.x:8000/
```

URL harus diakhiri `/`. Backend harus `--host 0.0.0.0`.

## Jalankan

**Android Studio:** Open folder ini → sync Gradle → Run ▶  

**CLI:**

```bash
gradlew.bat installDebug       # macOS/Linux: ./gradlew installDebug
```

## Troubleshooting singkat

| Masalah | Cek |
|---|---|
| Emulator tak connect | `DEV_BASE_URL=http://10.0.2.2:8000/` |
| HP tak connect | IP PC, firewall port 8000, satu Wi‑Fi |
| Google Sign-In gagal | Web client (`client_type` 3) di `google-services.json` |
