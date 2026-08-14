package com.example.scamshieldai.settings

import com.example.scamshieldai.network.AnalysisResult
import com.example.scamshieldai.network.ScamShieldRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object HistoryAutoCleaner {

    private val isoFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss"
    )

    /**
     * Hapus riwayat yang lebih tua dari [AppPreferences.AUTO_CLEAN_DAYS] hari.
     * Mengembalikan jumlah item yang berhasil dihapus.
     */
    suspend fun cleanOldHistory(repository: ScamShieldRepository): Int {
        val history = repository.getHistory(limit = 100).getOrElse { return 0 }
        val cutoffMs = System.currentTimeMillis() -
            TimeUnit.DAYS.toMillis(AppPreferences.AUTO_CLEAN_DAYS.toLong())
        var deleted = 0

        for (item in history.data) {
            val scanId = item.scanId ?: continue
            val createdMs = parseCreatedAtMillis(item) ?: continue
            if (createdMs < cutoffMs) {
                repository.deleteHistory(scanId).onSuccess { deleted++ }
            }
        }
        return deleted
    }

    private fun parseCreatedAtMillis(item: AnalysisResult): Long? {
        val raw = item.createdAt?.trim().orEmpty()
        if (raw.isEmpty()) return null

        for (pattern in isoFormats) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                    isLenient = true
                }
                val parsed: Date = sdf.parse(raw) ?: continue
                return parsed.time
            } catch (_: Exception) {
                // coba format berikutnya
            }
        }
        return null
    }
}
