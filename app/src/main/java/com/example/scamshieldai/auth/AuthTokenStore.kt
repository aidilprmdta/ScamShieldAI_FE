package com.example.scamshieldai.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AuthTokenStore {
    var idToken by mutableStateOf<String?>(null)
    var refreshToken: String? = null

    fun setToken(token: String, refresh: String? = null) {
        idToken = token
        if (refresh != null) refreshToken = refresh
    }

    fun clear() {
        idToken = null
        refreshToken = null
    }
}
