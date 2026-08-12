package com.example.scamshieldai.network

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(fallback: String): String {
    return when (this) {
        is ConnectException, is UnknownHostException ->
            "Tidak dapat terhubung ke server. Pastikan backend berjalan dan BASE_URL benar."
        is SocketTimeoutException ->
            "Koneksi timeout. Coba lagi."
        is IOException ->
            message?.takeIf { it.isNotBlank() } ?: "Koneksi gagal. Periksa jaringan Anda."
        else ->
            message?.takeIf { it.isNotBlank() } ?: fallback
    }
}
