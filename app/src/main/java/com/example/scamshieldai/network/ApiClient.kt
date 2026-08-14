package com.example.scamshieldai.network

import com.example.scamshieldai.BuildConfig
import com.example.scamshieldai.auth.AuthTokenStore
import okhttp3.Authenticator
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

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val tokenAuthenticator = object : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            if (responseCount(response) >= 2) return null

            val refreshToken = AuthTokenStore.refreshToken ?: return null

            val refreshCall = refreshApi.refreshToken(
                AuthRefreshRequest(refreshToken)
            ).execute()

            if (refreshCall.isSuccessful && refreshCall.body()?.success == true) {
                val tokens = refreshCall.body()!!.data
                AuthTokenStore.setToken(tokens.idToken, tokens.refreshToken)

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${tokens.idToken}")
                    .build()
            }

            return null
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
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ScamShieldApi = retrofit.create(ScamShieldApi::class.java)

    // Separate instance without authenticator to avoid recursion during refresh
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
}
