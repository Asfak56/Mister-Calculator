package com.example.mistercalculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class Theme {
    LIGHT,
    DARK
}

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4DDADA),
    onPrimary = Color.Black,
    background = Color(0xFF000000),
    onBackground = Color(0xFFF1FFFF),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFF1FFFF),
    secondary = Color.Gray
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A6A),
    onPrimary = Color.White,
    background = Color(0xFFF5FEFF),
    onBackground = Color(0xFF001F21),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF001F21)
)

@Composable
fun VoiceCalcTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        Theme.DARK ->
            DarkColorScheme

        Theme.LIGHT ->
            LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}