package com.example.scamshieldai.ui.feedback

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.scamshieldai.ui.theme.DangerRed
import com.example.scamshieldai.ui.theme.PrussianBlue
import com.example.scamshieldai.ui.theme.SafeGreen

enum class AppMessageKind {
    Success,
    Error,
    Info
}

data class AppSnackbarVisuals(
    override val message: String,
    val kind: AppMessageKind,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = true,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals

suspend fun SnackbarHostState.showSuccess(message: String) {
    showSnackbar(AppSnackbarVisuals(message = message, kind = AppMessageKind.Success))
}

suspend fun SnackbarHostState.showError(message: String) {
    showSnackbar(AppSnackbarVisuals(message = message, kind = AppMessageKind.Error))
}

suspend fun SnackbarHostState.showInfo(message: String) {
    showSnackbar(AppSnackbarVisuals(message = message, kind = AppMessageKind.Info))
}

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        val kind = (data.visuals as? AppSnackbarVisuals)?.kind ?: AppMessageKind.Info
        val container = when (kind) {
            AppMessageKind.Success -> SafeGreen
            AppMessageKind.Error -> DangerRed
            AppMessageKind.Info -> PrussianBlue
        }
        Snackbar(
            snackbarData = data,
            containerColor = container,
            contentColor = Color.White,
            actionColor = Color.White,
            dismissActionContentColor = Color.White
        )
    }
}
