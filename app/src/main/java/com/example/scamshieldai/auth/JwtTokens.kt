package com.example.scamshieldai.auth

import java.util.Base64

object JwtTokens {
    private const val DEFAULT_SKEW_SECONDS = 60L

    fun payloadJson(token: String): String? {
        val parts = token.split(".")
        if (parts.size < 2) return null
        val payload = parts[1]
        val padded = payload + "=".repeat((4 - payload.length % 4) % 4)
        return try {
            String(Base64.getUrlDecoder().decode(padded), Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }

    fun expiryEpochSeconds(token: String): Long? {
        val json = payloadJson(token) ?: return null
        return Regex("\"exp\"\\s*:\\s*(\\d+)").find(json)?.groupValues?.get(1)?.toLongOrNull()
    }

    fun isExpired(token: String, skewSeconds: Long = DEFAULT_SKEW_SECONDS): Boolean {
        if (token.isBlank() || token.count { it == '.' } != 2) return true
        val exp = expiryEpochSeconds(token) ?: return true
        val now = System.currentTimeMillis() / 1000
        return now >= (exp - skewSeconds)
    }
}
