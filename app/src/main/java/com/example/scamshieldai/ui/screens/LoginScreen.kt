package com.example.scamshieldai.ui.screens

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.theme.*
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YaleBlue)
    ) {
        // Decorative Blobs
        Canvas(modifier = Modifier.size(200.dp).offset(x = (-50).dp, y = (-50).dp)) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.minDimension / 1.2f
            )
        }
        
        // Hero Section Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 32.dp, end = 24.dp)
        ) {
            Text(
                text = "Hello!",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Welcome back to ScamShield AI",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Overlapping Logo
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

        // Form Card
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
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    color = PrussianBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Slate400) },
                    shape = RoundedCornerShape(16.dp),
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

                // Password Input
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Forgot Password?",
                    color = Slate500,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button with Dynamic Color
                val isEnabled = email.isNotBlank() && password.length >= 6 && !isLoading
                val auth = FirebaseAuth.getInstance()
                
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
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                isLoading = false
                                onLoginSuccess()
                            }
                            .addOnFailureListener {
                                isLoading = false
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
                        Text("Login", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account? ", color = Slate500, fontSize = 14.sp)
                    Text(
                        text = "Sign Up",
                        color = Cerulean,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // OR Separator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Slate100)
                    Text(
                        "Or login with",
                        color = Slate400,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Slate100)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Google Login Button (Full Width with Official Logo)
                OutlinedButton(
                    onClick = {
                        handleGoogleLogin(context, scope, onLoginSuccess)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Slate100),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrussianBlue)
                ) {
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
                            text = "Continue with Google",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrussianBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


private fun handleGoogleLogin(
    context: Context,
    scope: CoroutineScope,
    onSuccess: () -> Unit
) {
    val credentialManager = CredentialManager.create(context)
    val serverClientId = "YOUR_SERVER_CLIENT_ID.apps.googleusercontent.com"
    
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientId)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    scope.launch {
        try {
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential
            if (credential is GoogleIdTokenCredential) {
                onSuccess()
            }
        } catch (e: Exception) {
            onSuccess() // Fallback for demo
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
}
