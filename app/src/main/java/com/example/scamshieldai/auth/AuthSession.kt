package com.example.scamshieldai.auth

import com.example.scamshieldai.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AuthSession {
    suspend fun ensureValidToken(): Boolean = withContext(Dispatchers.IO) {
        val id = AuthTokenStore.idToken
        val refresh = AuthTokenStore.refreshToken
        if (id.isNullOrBlank() && refresh.isNullOrBlank()) return@withContext false
        if (!id.isNullOrBlank() && !JwtTokens.isExpired(id)) return@withContext true
        if (refresh.isNullOrBlank()) return@withContext false
        ApiClient.refreshAccessToken(force = true)
    }
}
