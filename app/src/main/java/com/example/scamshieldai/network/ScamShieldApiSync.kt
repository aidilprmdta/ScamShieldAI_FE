package com.example.scamshieldai.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ScamShieldApiSync {
    @POST("api/v1/auth/refresh")
    fun refreshToken(@Body request: AuthRefreshRequest): Call<AuthResponse>
}
