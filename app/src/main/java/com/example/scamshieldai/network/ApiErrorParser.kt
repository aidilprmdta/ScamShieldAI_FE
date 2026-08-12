package com.example.scamshieldai.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class ApiErrorBody(
    val success: Boolean = false,
    val error: ApiErrorDetail? = null,
    val message: String? = null,
)

data class ApiErrorDetail(
    val type: String? = null,
    val message: String? = null,
)

private val errorGson = Gson()

fun <T> Response<T>.apiErrorMessage(fallback: String): String {
    errorBody()?.string()?.let { raw ->
        runCatching { errorGson.fromJson(raw, ApiErrorBody::class.java) }.getOrNull()?.let { body ->
            body.error?.message?.takeIf { it.isNotBlank() }?.let { return it }
            body.message?.takeIf { it.isNotBlank() }?.let { return it }
        }
    }
    return fallback
}
