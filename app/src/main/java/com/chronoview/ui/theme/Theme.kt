package com.chronoview.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF263859),
    background = Color(0xFFF4F4F6),
    secondary = Color(0xFF7661E6),
    surface = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB8C6E0),
    background = Color(0xFF121316),
    secondary = Color(0xFF9F8BFF),
    surface = Color(0xFF1E2228)
)

@Composable
fun ChronoViewTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
