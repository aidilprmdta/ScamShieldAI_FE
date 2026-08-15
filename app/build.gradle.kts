import java.io.File
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

fun readGoogleWebClientId(googleServicesFile: File): String {
    if (!googleServicesFile.exists()) return ""
    val text = googleServicesFile.readText()
    // Look for client_id immediately followed by client_type 3
    val match = Regex("\"client_id\":\\s*\"([^\"]+)\",\\s*\"client_type\":\\s*3").find(text)
    if (match != null) return match.groupValues[1]
    
    // Fallback: split by curly braces to find the object containing client_type 3
    val blocks = text.split("{")
    for (block in blocks) {
        if (block.contains("\"client_type\": 3") || block.contains("\"client_type\":3")) {
            val idMatch = Regex("\"client_id\":\\s*\"([^\"]+)\"").find(block)
            if (idMatch != null) return idMatch.groupValues[1]
        }
    }
    return ""
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

fun normalizeBaseUrl(raw: String): String {
    val trimmed = raw.trim().trim('"')
    return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
}

val devBaseUrl = normalizeBaseUrl(
    localProperties.getProperty("DEV_BASE_URL", "http://192.168.1.8:8000/")
)

android {
    namespace = "com.example.scamshieldai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.scamshieldai"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"$devBaseUrl\"")
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"${readGoogleWebClientId(file("google-services.json"))}\""
        )
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"$devBaseUrl\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://api.scamshieldai.com/\"")
            optimization {
                enable = true
            }
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.mlkit.barcode)
    implementation(libs.mlkit.text)
    
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    
    implementation(libs.androidx.auth.credentials)
    implementation(libs.androidx.auth.credentials.play)
    implementation(libs.googleid.auth)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.play)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.datastore.preferences)
    implementation(libs.firebase.messaging)

    debugImplementation(libs.androidx.ui.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
