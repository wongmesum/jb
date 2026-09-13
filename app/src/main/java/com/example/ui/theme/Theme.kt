package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color(0xFF11140E),
    onSecondary = Color(0xFFFAF9F4),
    onTertiary = Color(0xFF11140E),
    onBackground = Color(0xFFE1E5DC),
    onSurface = Color(0xFFE1E5DC),
    surfaceVariant = Color(0xFF232B1E),
    onSurfaceVariant = Color(0xFFC1C7B8)
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF11140E),
    onBackground = Color(0xFF1B2215),
    onSurface = Color(0xFF1B2215),
    surfaceVariant = Color(0xFFECEDE5),
    onSurfaceVariant = Color(0xFF3B4832)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
