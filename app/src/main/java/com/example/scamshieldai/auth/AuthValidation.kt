package com.example.scamshieldai.auth

import android.util.Patterns

object AuthValidation {

    fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    fun emailError(email: String): String? = when {
        email.isBlank() -> null
        !isValidEmail(email) -> "Format email tidak valid"
        else -> null
    }

    fun passwordMatchError(password: String, confirmPassword: String): String? =
        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            "Password tidak cocok"
        } else {
            null
        }

    fun passwordStrengthError(password: String): String? = when {
        password.isEmpty() -> null
        password.length < 6 -> "Kata sandi minimal 6 karakter"
        else -> null
    }
}
