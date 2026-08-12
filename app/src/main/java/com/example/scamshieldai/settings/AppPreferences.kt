package com.example.scamshieldai.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "scamshield_prefs")

object AppPreferences {
    private val DARK_MODE = booleanPreferencesKey("dark_mode")
    private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    private val SAVED_TOKEN = stringPreferencesKey("saved_token")
    private val SAVED_REFRESH_TOKEN = stringPreferencesKey("saved_refresh_token")

    fun darkModeFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[DARK_MODE] ?: false }

    suspend fun setDarkMode(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    fun biometricEnabledFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[BIOMETRIC_ENABLED] ?: false }

    suspend fun setBiometricEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[BIOMETRIC_ENABLED] = enabled }
    }

    fun savedTokenFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { it[SAVED_TOKEN] }

    fun savedRefreshTokenFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { it[SAVED_REFRESH_TOKEN] }

    suspend fun setSavedToken(context: Context, token: String?, refreshToken: String? = null) {
        context.dataStore.edit {
            if (token == null) {
                it.remove(SAVED_TOKEN)
                it.remove(SAVED_REFRESH_TOKEN)
            } else {
                it[SAVED_TOKEN] = token
                if (refreshToken != null) it[SAVED_REFRESH_TOKEN] = refreshToken
            }
        }
    }
}
