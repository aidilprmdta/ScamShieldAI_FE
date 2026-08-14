package com.example.scamshieldai.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.scamshieldai.network.ScamShieldRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object GoogleSignInHelper {

    suspend fun signIn(
        context: Context,
        repository: ScamShieldRepository
    ): Result<Unit> {
        val activity = context.findActivity()
            ?: return Result.failure(Exception("Tidak dapat membuka login Google dari layar ini."))

        return try {
            val serverClientId = GoogleAuthHelper.getServerClientId(activity)
            val credentialManager = CredentialManager.create(activity)
            val googleCredential = requestGoogleCredential(activity, credentialManager, serverClientId)
            repository.googleLogin(googleCredential.idToken).map { Unit }
        } catch (e: GetCredentialCancellationException) {
            Result.failure(Exception("Login Google dibatalkan."))
        } catch (e: NoCredentialException) {
            Result.failure(Exception(noCredentialMessage()))
        } catch (e: GetCredentialException) {
            Result.failure(Exception(mapCredentialError(e)))
        } catch (e: Exception) {
            val mapped = e.message?.takeIf { it.isNotBlank() } ?: "Google login gagal"
            Result.failure(Exception(mapped))
        }
    }

    private suspend fun requestGoogleCredential(
        activity: Activity,
        credentialManager: CredentialManager,
        serverClientId: String,
    ): GoogleIdTokenCredential {
        return try {
            getCredential(
                activity,
                credentialManager,
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(serverClientId)
                    .setAutoSelectEnabled(false)
                    .build(),
            )
        } catch (_: NoCredentialException) {
            getCredential(
                activity,
                credentialManager,
                GetSignInWithGoogleOption.Builder(serverClientId).build(),
            )
        }
    }

    private suspend fun getCredential(
        activity: Activity,
        credentialManager: CredentialManager,
        option: CredentialOption,
    ): GoogleIdTokenCredential {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
        val result = credentialManager.getCredential(context = activity, request = request)
        return GoogleIdTokenCredential.createFrom(result.credential.data)
    }
}

private fun Context.findActivity(): Activity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

private fun noCredentialMessage(): String {
    return "Tidak ada akun Google yang bisa dipakai. Tambahkan akun Google di pengaturan HP, " +
        "pastikan Google Play Services aktif, lalu coba lagi."
}

private fun mapCredentialError(e: GetCredentialException): String {
    val msg = e.message.orEmpty().lowercase()
    return when {
        "canceled" in msg || "16:" in msg -> "Login Google dibatalkan."
        "no credential" in msg || e is NoCredentialException -> noCredentialMessage()
        "10:" in msg || "developer_error" in msg ->
            "Konfigurasi Google Sign-In belum lengkap. Daftarkan SHA-1 debug keystore di Firebase Console, lalu rebuild app."
        else -> e.message?.takeIf { it.isNotBlank() } ?: "Google login gagal"
    }
}
