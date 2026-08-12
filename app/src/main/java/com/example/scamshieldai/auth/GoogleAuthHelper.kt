package com.example.scamshieldai.auth

import android.content.Context
import com.example.scamshieldai.BuildConfig

/**
 * Web OAuth client ID for Google Sign-In via Credential Manager.
 * BuildConfig.GOOGLE_WEB_CLIENT_ID is extracted from google-services.json at build time.
 */
object GoogleAuthHelper {

    fun getServerClientId(context: Context): String {
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()) {
            return BuildConfig.GOOGLE_WEB_CLIENT_ID
        }

        val generatedId = context.resources
            .getIdentifier("default_web_client_id", "string", context.packageName)
        if (generatedId != 0) {
            val value = context.getString(generatedId)
            if (value.isNotBlank()) return value
        }

        throw IllegalStateException(
            "Google Sign-In belum dikonfigurasi. Aktifkan Google di Firebase Console " +
                "(Authentication > Sign-in method > Google), lalu rebuild app."
        )
    }
}
