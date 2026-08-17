package com.example.scamshieldai.network

import com.example.scamshieldai.BuildConfig
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.auth.JwtTokens
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val BASE_URL = BuildConfig.BASE_URL
    private val refreshLock = Any()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val authHeaderInterceptor = Interceptor { chain ->
        var request = chain.request()
        val auth = request.header("Authorization")
        if (auth != null && auth.startsWith("Bearer ", ignoreCase = true)) {
            val sent = auth.substringAfter(" ").trim()
            if (JwtTokens.isExpired(sent)) {
                refreshAccessToken(force = false)
            }
            val latest = AuthTokenStore.idToken
            if (!latest.isNullOrBlank() && latest != sent) {
                request = request.newBuilder()
                    .header("Authorization", "Bearer $latest")
                    .build()
            }
        }
        chain.proceed(request)
    }

    private val tokenAuthenticator = object : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            if (responseCount(response) >= 2) return null
            if (response.request.url.encodedPath.contains("/auth/refresh")) return null

            val failedToken = response.request.header("Authorization")
                ?.substringAfter("Bearer ", "")
                ?.trim()
                .orEmpty()

            val newToken = synchronized(refreshLock) {
                val latest = AuthTokenStore.idToken
                if (!latest.isNullOrBlank() && latest != failedToken && !JwtTokens.isExpired(latest)) {
                    latest
                } else if (performRefresh()) {
                    AuthTokenStore.idToken
                } else {
                    null
                }
            } ?: return null

            return response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }

        private fun responseCount(response: Response): Int {
            var count = 1
            var prior = response.priorResponse
            while (prior != null) {
                count++
                prior = prior.priorResponse
            }
            return count
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authHeaderInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ScamShieldApi = retrofit.create(ScamShieldApi::class.java)

    private val refreshClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val refreshRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(refreshClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val refreshApi: ScamShieldApiSync = refreshRetrofit.create(ScamShieldApiSync::class.java)

    fun refreshAccessToken(force: Boolean = false): Boolean {
        synchronized(refreshLock) {
            val current = AuthTokenStore.idToken
            if (!force && !current.isNullOrBlank() && !JwtTokens.isExpired(current)) {
                return true
            }
            return performRefresh()
        }
    }

    private fun performRefresh(): Boolean {
        val refreshToken = AuthTokenStore.refreshToken ?: return false
        return try {
            val refreshCall = refreshApi.refreshToken(AuthRefreshRequest(refreshToken)).execute()
            if (refreshCall.isSuccessful && refreshCall.body()?.success == true) {
                val tokens = refreshCall.body()!!.data
                AuthTokenStore.setToken(tokens.idToken, tokens.refreshToken)
                true
            } else {
                if (refreshCall.code() == 401 || refreshCall.code() == 400) {
                    AuthTokenStore.clear()
                }
                false
            }
        } catch (_: Exception) {
            false
        }
    }
}
