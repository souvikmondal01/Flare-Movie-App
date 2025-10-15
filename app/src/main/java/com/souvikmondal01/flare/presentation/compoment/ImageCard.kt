package com.souvikmondal01.flare.presentation.compoment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageCard(
    imagePath: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    onClick: () -> Unit = {},
    height: Dp = 152.dp,
    width: Dp = 104.dp,
    cornerRadius: Dp = 6.dp,
    contentScale: ContentScale = ContentScale.Crop,
    placeHolderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = .1f),
    alpha: Float = 1f,
    shape: RoundedCornerShape = RoundedCornerShape(cornerRadius),
    clickable: Boolean = true,
) {
    val model = ImageRequest.Builder(LocalContext.current)
        .data(imagePath).crossfade(true).build()
    if (imagePath.isNotEmpty()) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
                .height(height)
                .width(width)
                .clip(shape)
                .background(placeHolderColor)
                .clickable(
                    enabled = clickable
                ) {
                    onClick()
                },
            alpha = alpha,
        )
    } else {
        Box(
            modifier = modifier
                .height(height)
                .width(width)
                .clip(shape)
                .background(color = backgroundColor)
                .clickable(
                    enabled = clickable
                ) { onClick() },
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                contentDescription,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

}