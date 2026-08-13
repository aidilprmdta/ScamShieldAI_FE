package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.auth.AuthValidation
import com.example.scamshieldai.ui.theme.*

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onSubmit: (
        currentPassword: String,
        newPassword: String,
        onDone: (Result<Unit>) -> Unit
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val newPasswordError = AuthValidation.passwordStrengthError(newPassword)
    val confirmError = AuthValidation.passwordMatchError(newPassword, confirmPassword)
    val sameAsOldError =
        if (newPassword.isNotEmpty() && newPassword == currentPassword) {
            "Kata sandi baru harus berbeda"
        } else {
            null
        }

    val isEnabled = currentPassword.isNotBlank() &&
        newPassword.length >= 6 &&
        newPassword == confirmPassword &&
        newPassword != currentPassword &&
        !isLoading

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    enabled = !isLoading,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DeepNavy.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = PrussianBlue)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Ubah Kata Sandi",
                    color = PrussianBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Masukkan kata sandi saat ini, lalu tentukan kata sandi baru untuk akun email Anda.",
                    color = Slate500,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                PasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Kata sandi saat ini",
                    visible = currentVisible,
                    onToggleVisible = { currentVisible = !currentVisible },
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Kata sandi baru",
                    visible = newVisible,
                    onToggleVisible = { newVisible = !newVisible },
                    enabled = !isLoading,
                    isError = newPasswordError != null || sameAsOldError != null,
                    supportingText = newPasswordError ?: sameAsOldError
                )
                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Konfirmasi kata sandi baru",
                    visible = confirmVisible,
                    onToggleVisible = { confirmVisible = !confirmVisible },
                    enabled = !isLoading,
                    isError = confirmError != null,
                    supportingText = confirmError
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Minimal 6 karakter. Jika Anda login dengan Google, ubah keamanan lewat akun Google.",
                    color = Slate400,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        onSubmit(currentPassword, newPassword) { result ->
                            isLoading = false
                            result.onFailure {
                                errorMessage = it.message ?: "Gagal mengubah kata sandi"
                            }
                        }
                    },
                    enabled = isEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Cerulean,
                        disabledContainerColor = DeepNavy.copy(alpha = 0.08f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Simpan Kata Sandi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 88.dp)
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisible: () -> Unit,
    enabled: Boolean,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Slate400) },
        trailingIcon = {
            IconButton(onClick = onToggleVisible) {
                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (visible) "Sembunyikan" else "Tampilkan",
                    tint = Slate400
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = enabled,
        isError = isError,
        supportingText = supportingText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = YaleBlue,
            unfocusedBorderColor = Slate100,
            focusedLabelColor = YaleBlue,
            cursorColor = Cerulean,
            focusedTextColor = PrussianBlue,
            unfocusedTextColor = PrussianBlue
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordPreview() {
    ChangePasswordScreen(onBack = {}, onSubmit = { _, _, done -> done(Result.success(Unit)) })
}
