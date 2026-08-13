package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.ui.components.ScreenTopBar
import com.example.scamshieldai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanChatScreen(
    onBack: () -> Unit,
    onAnalyze: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by rememberSaveable { mutableStateOf("") }
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        ScreenTopBar(
            title = "Scan chat",
            subtitle = "Tempel teks pesan yang ingin dicek",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .border(1.dp, DeepNavy.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                placeholder = {
                    Text(
                        "Tempel teks pesan di sini...",
                        color = Slate500.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Cerulean,
                    focusedTextColor = PrussianBlue,
                    unfocusedTextColor = PrussianBlue
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${inputText.length} karakter",
                    color = Slate500,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier.clickable {
                        clipboardManager.getText()?.let { inputText = it.text }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = null,
                        tint = Cerulean,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tempel",
                        color = Cerulean,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        val isEnabled = inputText.isNotBlank()
        Button(
            onClick = { onAnalyze(inputText) },
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cerulean,
                disabledContainerColor = DeepNavy.copy(alpha = 0.08f)
            )
        ) {
            Text("Analisis", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScanChatPreview() {
    ScanChatScreen(onBack = {}, onAnalyze = {})
}
