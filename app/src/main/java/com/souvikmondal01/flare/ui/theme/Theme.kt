package com.souvikmondal01.flare.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Red,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black2,
)

@Composable
fun PhaseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}