package com.example.scamshieldai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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

private val DarkColorScheme = darkColorScheme(
    primary = Cerulean,
    secondary = YaleBlue,
    tertiary = DeepNavy,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = WhiteBackground,
    onSecondary = WhiteBackground,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
)

@Composable
fun ScamShieldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
