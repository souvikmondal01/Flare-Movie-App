package com.kivous.phasemovie.presentation.compoment

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kivous.phasemovie.domain.model.SliderMovie
import kotlinx.coroutines.delay

@Composable
fun ImageSlider(
    sliderMovieList: List<SliderMovie>,
    autoScroll: Boolean = false,
) {
    val pagerState = rememberPagerState(pageCount = { sliderMovieList.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Background image
            val bgPhotoUrl = sliderMovieList[pagerState.currentPage].bgUrl
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(bgPhotoUrl).crossfade(true)
                    .build(),
                contentDescription = "Background image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Shadow Effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadowEffect()
            )

            // Auto scroll
            if (autoScroll) {
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(3000)
                        val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            }

            // Slider
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    // Foreground image
                    val fgPhotoUrl = sliderMovieList[page].fgUrl
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(fgPhotoUrl)
                            .crossfade(true).build(),
                        contentDescription = "Foreground image",
                        modifier = Modifier
                            .width(144.dp)
                            .padding(bottom = 32.dp),
                    )
                }

            }

//          Genres
            Text(
                text = sliderMovieList[pagerState.currentPage].genres.joinToString(separator = "  •  "),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
                    .horizontalScroll(rememberScrollState()),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(height = 8.dp)

        // Indicator
        PagerIndicator(size = pagerState.pageCount, currentPage = pagerState.currentPage)
    }

}

@Composable
fun PagerIndicator(
    size: Int,
    currentPage: Int,
    selectedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    unselectedIndicatorColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    Row {
        repeat(size) {
            val color by animateColorAsState(
                targetValue = if (it == currentPage) selectedIndicatorColor
                else unselectedIndicatorColor.copy(alpha = 0.8f),
            )

            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
