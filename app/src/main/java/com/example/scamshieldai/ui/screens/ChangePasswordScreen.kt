package com.example.scamshieldai.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.theme.*

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onChangePassword: (currentPassword: String, newPassword: String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
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

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
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
                            "Perbarui Kata Sandi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
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
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordPreview() {
    ChangePasswordScreen(
        onBack = {},
        onChangePassword = { _, _ -> }
    )
}
