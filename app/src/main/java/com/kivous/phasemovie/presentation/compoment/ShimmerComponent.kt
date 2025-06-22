package com.kivous.phasemovie.presentation.compoment

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    contentPadding: PaddingValues = PaddingValues(),
    containerAlpha: Float = 0.5f,
    shimmerColor: Color = contentColor.darken(),
    shimmerAlpha: Float = containerAlpha,
    shimmerDuration: Int = 3000,
    text: String = "ShimmerBox",
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    textColor: Color = contentColor,
    cornerRadius: Dp = 8.dp,
    borderStroke: Dp = 1.dp,
    borderColor: Color = contentColor,
    onClick: () -> Unit = {},
    content: (@Composable () -> Unit)? = {}
) {
    Box(
        modifier = modifier
            .clickable {
                onClick()
            }
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = borderStroke, brush = Brush.horizontalGradient(
                    0f to borderColor.darken(),
                    0.5f to borderColor,
                    1f to borderColor.darken(),
                ), shape = RoundedCornerShape(cornerRadius)
            )
            .background(color = containerColor.copy(alpha = containerAlpha))
            .shimmerEffect(
                color = shimmerColor.copy(alpha = shimmerAlpha), animationDuration = shimmerDuration
            ), contentAlignment = contentAlignment
    ) {
        Column(
            modifier = Modifier.padding(paddingValues = contentPadding),
        ) {
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    color = textColor,
                    style = textStyle
                )
            }
            content?.invoke()
        }

    }
}

fun Color.darken(factor: Float = 0.75f): Color {
    val argb = this.toArgb()
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(argb, hsl)
    hsl[2] = (hsl[2] * factor).coerceIn(0f, 1f) // reduce lightness
    val darkenedArgb = ColorUtils.HSLToColor(hsl)
    return Color(darkenedArgb)
}

@Composable
fun Modifier.shimmerEffect(
    color: Color = MaterialTheme.colorScheme.onBackground,
    animationDuration: Int = 3000
): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(animationDuration)),
        label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                color,
                Color.Transparent
            ),
            start = Offset(startOffsetX, size.height.toFloat()),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

