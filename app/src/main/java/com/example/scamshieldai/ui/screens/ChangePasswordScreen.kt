package com.example.scamshieldai.ui.screens

<<<<<<< HEAD
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
=======
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
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
<<<<<<< HEAD
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
=======
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import androidx.compose.ui.zIndex
import com.example.scamshieldai.R
=======
import com.example.scamshieldai.auth.AuthValidation
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
import com.example.scamshieldai.ui.theme.*

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
<<<<<<< HEAD
    onChangePassword: (currentPassword: String, newPassword: String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
=======
    onSubmit: (
        currentPassword: String,
        newPassword: String,
        onDone: (Result<Unit>) -> Unit
    ) -> Unit,
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
    modifier: Modifier = Modifier
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
<<<<<<< HEAD
    
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Validation
    val isCurrentPasswordValid = currentPassword.length >= 6
    val isNewPasswordValid = newPassword.length >= 6
    val isConfirmPasswordValid = confirmPassword == newPassword && confirmPassword.isNotEmpty()
    val isFormValid = isCurrentPasswordValid && isNewPasswordValid && isConfirmPasswordValid && !isLoading
=======
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
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
<<<<<<< HEAD
=======
            errorMessage = null
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
<<<<<<< HEAD
            .background(YaleBlue)
    ) {
        // Background Decorations
        Canvas(modifier = Modifier.size(200.dp).offset(x = 250.dp, y = (-50).dp)) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.minDimension / 1.2f
            )
        }

        // Header Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Keamanan Akun",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Perbarui kata sandi Anda secara berkala",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Floating Logo
        Image(
            painter = painterResource(id = R.drawable.logoapp_removebg),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(top = 170.dp, end = 32.dp)
                .size(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.TopEnd)
                .zIndex(1f)
        )

        // Form Container
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Ubah Kata Sandi",
                    color = PrussianBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                // Current Password
                PasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Kata Sandi Saat Ini",
                    showPassword = showCurrentPassword,
                    onTogglePassword = { showCurrentPassword = !showCurrentPassword },
                    focusManager = focusManager,
                    errorText = if (!isCurrentPasswordValid && currentPassword.isNotEmpty()) "Minimal 6 karakter" else null
                )

                Spacer(modifier = Modifier.height(20.dp))

                // New Password
                PasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Kata Sandi Baru",
                    showPassword = showNewPassword,
                    onTogglePassword = { showNewPassword = !showNewPassword },
                    focusManager = focusManager,
                    errorText = if (!isNewPasswordValid && newPassword.isNotEmpty()) "Minimal 6 karakter" else null
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password
                PasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Konfirmasi Kata Sandi Baru",
                    showPassword = showConfirmPassword,
                    onTogglePassword = { showConfirmPassword = !showConfirmPassword },
                    focusManager = focusManager,
                    imeAction = ImeAction.Done,
                    onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) onChangePassword(currentPassword, newPassword)
                    },
                    errorText = if (!isConfirmPasswordValid && confirmPassword.isNotEmpty()) "Kata sandi tidak cocok" else null
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Submit Button
                val buttonColor by animateColorAsState(
                    targetValue = if (isFormValid) YaleBlue else Slate100,
                    label = "buttonColor"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isFormValid) Color.White else Slate500,
                    label = "contentColor"
                )

                Button(
                    onClick = { onChangePassword(currentPassword, newPassword) },
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
=======
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
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
<<<<<<< HEAD
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Perbarui Kata Sandi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
=======
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Simpan Kata Sandi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
<<<<<<< HEAD
            modifier = Modifier.align(Alignment.BottomCenter)
=======
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 88.dp)
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
<<<<<<< HEAD
    showPassword: Boolean,
    onTogglePassword: () -> Unit,
    focusManager: FocusManager,
    imeAction: ImeAction = ImeAction.Next,
    onDone: (() -> Unit)? = null,
    errorText: String? = null
) {
    Column {
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
=======
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
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordPreview() {
<<<<<<< HEAD
    ChangePasswordScreen(
        onBack = {},
        onChangePassword = { _, _ -> }
    )
=======
    ChangePasswordScreen(onBack = {}, onSubmit = { _, _, done -> done(Result.success(Unit)) })
>>>>>>> dc5197f460afaf389538d47847d860f9e8cc625d
}
