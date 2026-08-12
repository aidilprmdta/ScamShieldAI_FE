package com.example.scamshieldai.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.scamshieldai.network.ScamShieldRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object GoogleSignInHelper {

    suspend fun signIn(
        context: Context,
        repository: ScamShieldRepository
    ): Result<Unit> {
        return try {
            val credentialManager = CredentialManager.create(context)
            val serverClientId = GoogleAuthHelper.getServerClientId(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context = context, request = request)
            val googleCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            repository.googleLogin(googleCredential.idToken).map { Unit }
        } catch (e: Exception) {
            Result.failure(
                if (e.message?.isNotBlank() == true) e
                else Exception("Google login gagal", e)
            )
        }
    }
}
