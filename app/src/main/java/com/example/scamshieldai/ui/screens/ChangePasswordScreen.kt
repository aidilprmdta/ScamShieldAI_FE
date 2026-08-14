package com.example.scamshieldai.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    val newPasswordError = AuthValidation.passwordStrengthError(newPassword)
    val confirmError = AuthValidation.passwordMatchError(newPassword, confirmPassword)
    val sameAsOldError = if (newPassword.isNotEmpty() && newPassword == currentPassword) {
        "Kata sandi baru harus berbeda"
    } else {
        null
    }

    val isFormValid = currentPassword.isNotBlank() &&
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
                    .imePadding()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Masukkan kata sandi saat ini, lalu buat kata sandi baru.",
                    color = Slate500,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                PasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Kata Sandi Saat Ini",
                    showPassword = showCurrentPassword,
                    onTogglePassword = { showCurrentPassword = !showCurrentPassword },
                    focusManager = focusManager,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Kata Sandi Baru",
                    showPassword = showNewPassword,
                    onTogglePassword = { showNewPassword = !showNewPassword },
                    focusManager = focusManager,
                    enabled = !isLoading,
                    errorText = newPasswordError ?: sameAsOldError
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Konfirmasi Kata Sandi Baru",
                    showPassword = showConfirmPassword,
                    onTogglePassword = { showConfirmPassword = !showConfirmPassword },
                    focusManager = focusManager,
                    imeAction = ImeAction.Done,
                    enabled = !isLoading,
                    onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) {
                            isLoading = true
                            onSubmit(currentPassword, newPassword) { result ->
                                isLoading = false
                                result.onFailure {
                                    errorMessage = it.message ?: "Gagal mengubah kata sandi"
                                }
                            }
                        }
                    },
                    errorText = confirmError
                )

                Spacer(modifier = Modifier.height(32.dp))

                val buttonColor by animateColorAsState(
                    targetValue = if (isFormValid) YaleBlue else Slate100,
                    label = "buttonColor"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isFormValid) Color.White else Slate500,
                    label = "contentColor"
                )

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isFormValid,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = contentColor,
                        disabledContainerColor = buttonColor,
                        disabledContentColor = contentColor
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Simpan Perubahan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onTogglePassword: () -> Unit,
    focusManager: FocusManager,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onDone: (() -> Unit)? = null,
    errorText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Slate400) },
        trailingIcon = {
            IconButton(onClick = onTogglePassword) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Slate400
                )
            }
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(16.dp),
        isError = errorText != null,
        enabled = enabled,
        supportingText = errorText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = YaleBlue,
            unfocusedBorderColor = Slate100,
            focusedLabelColor = YaleBlue,
            unfocusedContainerColor = Slate100.copy(alpha = 0.3f),
            focusedContainerColor = Slate100.copy(alpha = 0.1f)
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { onDone?.invoke() }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordPreview() {
    ChangePasswordScreen(
        onBack = {},
        onSubmit = { _, _, done -> done(Result.success(Unit)) }
    )
}
