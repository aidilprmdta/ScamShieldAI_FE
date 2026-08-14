package com.example.scamshieldai.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot

object AuthTokenStore {
    private val idTokenState = mutableStateOf<String?>(null)

    var idToken: String?
        get() = idTokenState.value
        private set(value) {
            Snapshot.withMutableSnapshot {
                idTokenState.value = value
            }
        }

    @Volatile
    var refreshToken: String? = null
        private set

    @Volatile
    var persistHandler: ((idToken: String?, refreshToken: String?) -> Unit)? = null

    fun setToken(token: String, refresh: String? = null) {
        idToken = token
        if (refresh != null) refreshToken = refresh
        persistHandler?.invoke(idToken, refreshToken)
    }

    fun restore(token: String?, refresh: String?) {
        idToken = token
        refreshToken = refresh
    }

    fun clear() {
        idToken = null
        refreshToken = null
        persistHandler?.invoke(null, null)
    }
}
