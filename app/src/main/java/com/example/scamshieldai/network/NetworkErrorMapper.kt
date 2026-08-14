package com.example.scamshieldai.network

import com.example.scamshieldai.BuildConfig
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(fallback: String): String {
    val server = BuildConfig.BASE_URL.trimEnd('/')
    return when (this) {
        is ConnectException, is UnknownHostException ->
            "Tidak dapat terhubung ke $server. Pastikan backend berjalan (host 0.0.0.0), " +
                "HP dan PC di WiFi yang sama, lalu rebuild app jika IP PC berubah."
        is SocketTimeoutException ->
            "Koneksi timeout ke $server. HP kemungkinan beda jaringan dengan PC. " +
                "Samakan WiFi, update DEV_BASE_URL di local.properties, lalu rebuild."
        is IOException ->
            message?.takeIf { it.isNotBlank() } ?: "Koneksi gagal. Periksa jaringan Anda."
        else ->
            message?.takeIf { it.isNotBlank() } ?: fallback
    }
}
