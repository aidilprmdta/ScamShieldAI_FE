package com.example.scamshieldai.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scamshieldai.R
import com.example.scamshieldai.auth.AuthValidation
import com.example.scamshieldai.auth.GoogleSignInHelper
import com.example.scamshieldai.network.ScamShieldRepository
import com.example.scamshieldai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { ScamShieldRepository() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val trimmedEmail = email.trim()
    val emailError = AuthValidation.emailError(trimmedEmail)
    val passwordMatchError = AuthValidation.passwordMatchError(password, confirmPassword)
    val isEnabled = trimmedEmail.isNotBlank() &&
        emailError == null &&
        password.length >= 6 &&
        password == confirmPassword &&
        !isLoading &&
        !isGoogleLoading

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YaleBlue)
    ) {
        Canvas(modifier = Modifier.size(200.dp).offset(x = 250.dp, y = (-50).dp)) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.minDimension / 1.2f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, start = 32.dp, end = 24.dp)
        ) {
            Text(
                text = "ScamShield",
                color = Color.White,
                fontFamily = DisplayFontFamily,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Buat akun untuk menyimpan riwayat",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Image(
            painter = painterResource(id = R.drawable.logoapp_removebg),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(top = 180.dp, end = 32.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.TopEnd)
                .zIndex(1f)
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 230.dp),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToLogin() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Slate400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kembali ke login",
                        color = Slate400,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Daftar",
                    color = PrussianBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Slate400) },
                    shape = RoundedCornerShape(16.dp),
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YaleBlue,
                        unfocusedBorderColor = Slate100,
                        focusedLabelColor = YaleBlue,
                        unfocusedContainerColor = Slate100.copy(alpha = 0.3f),
                        focusedContainerColor = Slate100.copy(alpha = 0.1f)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Slate400) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Slate400
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    supportingText = {
                        Text(
                            text = "Minimal 6 karakter",
                            color = Slate400,
                            fontSize = 12.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YaleBlue,
                        unfocusedBorderColor = Slate100,
                        focusedLabelColor = YaleBlue,
                        unfocusedContainerColor = Slate100.copy(alpha = 0.3f),
                        focusedContainerColor = Slate100.copy(alpha = 0.1f)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Konfirmasi Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Slate400) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Slate400
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordMatchError != null,
                    supportingText = passwordMatchError?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YaleBlue,
                        unfocusedBorderColor = Slate100,
                        focusedLabelColor = YaleBlue,
                        unfocusedContainerColor = Slate100.copy(alpha = 0.3f),
                        focusedContainerColor = Slate100.copy(alpha = 0.1f)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(32.dp))

                val buttonColor by animateColorAsState(
                    targetValue = if (isEnabled) YaleBlue else Slate100,
                    label = "buttonColor"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isEnabled) Color.White else Slate500,
                    label = "contentColor"
                )

                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            repository.register(trimmedEmail, password)
                                .onSuccess {
                                    isLoading = false
                                    onRegisterSuccess()
                                }
                                .onFailure {
                                    isLoading = false
                                    errorMessage = it.message ?: "Registrasi gagal"
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = contentColor,
                        disabledContainerColor = buttonColor,
                        disabledContentColor = contentColor
                    ),
                    enabled = isEnabled
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Daftar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Sudah punya akun? ", color = Slate500, fontSize = 14.sp)
                    Text(
                        text = "Masuk",
                        color = Cerulean,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Slate100)
                    Text(
                        "Atau daftar dengan",
                        color = Slate400,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Slate100)
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        isGoogleLoading = true
                        scope.launch {
                            GoogleSignInHelper.signIn(context, repository)
                                .onSuccess {
                                    isGoogleLoading = false
                                    onRegisterSuccess()
                                }
                                .onFailure {
                                    isGoogleLoading = false
                                    errorMessage = it.message ?: "Google login gagal"
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Slate100),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrussianBlue),
                    enabled = !isLoading && !isGoogleLoading
                ) {
                    if (isGoogleLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google_logo),
                                contentDescription = "Google Logo",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Lanjut dengan Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrussianBlue
                            )
                        }
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

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
}
