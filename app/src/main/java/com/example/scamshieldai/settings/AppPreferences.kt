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
    private val SAVED_TOKEN = stringPreferencesKey("saved_token")
    private val SAVED_REFRESH_TOKEN = stringPreferencesKey("saved_refresh_token")
    private val NOTIF_ALL = booleanPreferencesKey("notif_all")
    private val NOTIF_SECURITY = booleanPreferencesKey("notif_security")
    private val NOTIF_EDUCATION = booleanPreferencesKey("notif_education")
    private val NOTIF_SYSTEM = booleanPreferencesKey("notif_system")
    private val AUTO_CLEAN_ENABLED = booleanPreferencesKey("auto_clean_enabled")

    const val AUTO_CLEAN_DAYS = 30

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

    fun notifAllFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[NOTIF_ALL] ?: true }

    fun notifSecurityFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[NOTIF_SECURITY] ?: true }

    fun notifEducationFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[NOTIF_EDUCATION] ?: true }

    fun notifSystemFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[NOTIF_SYSTEM] ?: false }

    suspend fun setNotifAll(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[NOTIF_ALL] = enabled }
    }

    suspend fun setNotifSecurity(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[NOTIF_SECURITY] = enabled }
    }

    suspend fun setNotifEducation(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[NOTIF_EDUCATION] = enabled }
    }

    suspend fun setNotifSystem(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[NOTIF_SYSTEM] = enabled }
    }

    fun autoCleanEnabledFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[AUTO_CLEAN_ENABLED] ?: false }

    suspend fun setAutoCleanEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[AUTO_CLEAN_ENABLED] = enabled }
    }
}
