package com.kivous.phasemovie.presentation.compoment

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.shadowEffect(
    color: Color = MaterialTheme.colorScheme.background
): Modifier = composed {
    background(
        brush = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to color,
                0.3f to Color.Transparent,
                0.5f to Color.Transparent,
                0.9f to color.copy(alpha = 0.85f),
                0.97f to color.copy(alpha = 0.95f),
                1.0f to color,
            )
        )
    )
}