package com.example.scamshieldai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Cerulean,
    secondary = YaleBlue,
    tertiary = DeepNavy,
    background = WhiteBackground,
    surface = CardWhite,
    onPrimary = WhiteBackground,
    onSecondary = WhiteBackground,
    onBackground = PrussianBlue,
    onSurface = PrussianBlue,
)

@Composable
fun ScamShieldTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = ScamShieldTypography,
        content = content
    )
}
