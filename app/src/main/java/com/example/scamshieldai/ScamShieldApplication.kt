package com.example.scamshieldai

import android.app.Application
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.settings.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ScamShieldApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        AuthTokenStore.persistHandler = { idToken, refreshToken ->
            applicationScope.launch {
                AppPreferences.setSavedToken(this@ScamShieldApplication, idToken, refreshToken)
            }
        }
    }
}
