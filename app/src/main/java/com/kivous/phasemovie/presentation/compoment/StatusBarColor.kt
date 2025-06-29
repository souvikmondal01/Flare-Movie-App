package com.kivous.phasemovie.presentation.compoment

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBarColor(color: Color = MaterialTheme.colorScheme.background) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color)
}

@Composable
fun StatusBarColor(
    scrollState: Int = 0,
    threshold: Int = 200,
    color: Color = MaterialTheme.colorScheme.background,
) {
    val systemUiController = rememberSystemUiController()

    val statusBarColor = remember(scrollState, color) {
        if (scrollState > threshold) color.copy(alpha = 0.9f)
        else Color.Transparent
    }

    SideEffect {
        systemUiController.setSystemBarsColor(statusBarColor)
    }
}
