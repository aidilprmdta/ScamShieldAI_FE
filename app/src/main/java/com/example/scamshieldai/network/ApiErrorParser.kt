package com.example.scamshieldai.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class ApiErrorBody(
    val success: Boolean = false,
    val error: ApiErrorDetail? = null,
    val message: String? = null,
    val detail: Any? = null,
)

data class ApiErrorDetail(
    val type: String? = null,
    val message: String? = null,
)

private val errorGson = Gson()

private fun parseFastApiDetail(detail: Any?): String? {
    if (detail !is List<*>) return null
    val messages = detail.mapNotNull { item ->
        when (item) {
            is Map<*, *> -> item["msg"]?.toString()?.takeIf { it.isNotBlank() }
            else -> null
        }
    }
    return messages.firstOrNull()?.takeIf { it.isNotBlank() }
}

fun <T> Response<T>.apiErrorMessage(fallback: String): String {
    errorBody()?.string()?.let { raw ->
        runCatching { errorGson.fromJson(raw, ApiErrorBody::class.java) }.getOrNull()?.let { body ->
            body.error?.message?.takeIf { it.isNotBlank() }?.let { return it }
            body.message?.takeIf { it.isNotBlank() }?.let { return it }
            parseFastApiDetail(body.detail)?.let { return it }
        }
    }
    return fallback
}
