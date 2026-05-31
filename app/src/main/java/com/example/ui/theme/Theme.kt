package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CosmicCyan,
    onPrimary = SpaceDeepAbyss,
    secondary = CosmicTeal,
    onSecondary = SmoothWhite,
    tertiary = CosmicLavender,
    background = SpaceDeepAbyss,
    onBackground = IceGreyText,
    surface = SpaceDarkSteel,
    onSurface = SmoothWhite,
    surfaceVariant = SpaceCardBg,
    onSurfaceVariant = IceGreyText
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
