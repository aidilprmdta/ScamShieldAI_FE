package com.example.scamshieldai.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.auth.AuthValidation
import com.example.scamshieldai.ui.theme.*

@Composable
fun EditProfileScreen(
    initialName: String,
    initialEmail: String,
    onBack: () -> Unit,
    onSave: (name: String, email: String, onDone: (Result<Unit>) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(initialName) { mutableStateOf(initialName) }
    var email by remember(initialEmail) { mutableStateOf(initialEmail) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val trimmedName = name.trim()
    val trimmedEmail = email.trim()
    val emailError = AuthValidation.emailError(trimmedEmail)
    val nameError = when {
        trimmedName.isBlank() -> null
        trimmedName.length < 2 -> "Nama minimal 2 karakter"
        else -> null
    }
    val hasChanges = trimmedName != initialName.trim() || trimmedEmail != initialEmail.trim()
    val isEnabled = trimmedName.length >= 2 &&
        emailError == null &&
        trimmedEmail.isNotBlank() &&
        hasChanges &&
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
                    text = "Edit Profil",
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
                    text = "Perbarui nama dan email akun Anda.",
                    color = Slate500,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nama") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = Slate400) },
                    shape = RoundedCornerShape(16.dp),
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YaleBlue,
                        unfocusedBorderColor = Slate100,
                        focusedLabelColor = YaleBlue,
                        unfocusedContainerColor = Slate100.copy(alpha = 0.3f),
                        focusedContainerColor = Slate100.copy(alpha = 0.1f)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                        onSave(trimmedName, trimmedEmail) { result ->
                            isLoading = false
                            result
                                .onSuccess { onBack() }
                                .onFailure { errorMessage = it.message ?: "Gagal menyimpan profil" }
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
                        Text("Simpan Perubahan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
private fun EditProfilePreview() {
    EditProfileScreen(
        initialName = "Pramadya",
        initialEmail = "pramadytaa@gmail.com",
        onBack = {},
        onSave = { _, _, done -> done(Result.success(Unit)) }
    )
}
