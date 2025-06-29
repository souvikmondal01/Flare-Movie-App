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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kivous.phasemovie.domain.model.Movie
import kotlinx.coroutines.delay

@Composable
fun ImageSlider(
    movies: List<Movie> = emptyList(),
    autoScroll: Boolean = false,
    showPlaceholder: Boolean = true,
    placeHolderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
) {
    if (movies.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { movies.size })
        val currentMovie = movies[pagerState.currentPage]
        val textScrollState = rememberScrollState()

//      Auto scroll effect
        LaunchedEffect(pagerState, autoScroll) {
            if (autoScroll) {
                while (true) {
                    delay(5000)
                    val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Background image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentMovie.backdropPath).crossfade(true).build(),
                    contentDescription = "Backdrop for ${currentMovie.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

//              Shadow effect overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadowEffect()
                )

//              Slider
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
//                      Foreground image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(movies[page].posterPath).crossfade(true).build(),
                            contentDescription = "Poster for ${movies[page].title}",
                            modifier = Modifier
                                .width(144.dp)
                                .padding(bottom = 32.dp),
                        )
                    }

                }

//              Genres
                Text(
                    text = currentMovie.genres.joinToString(separator = "  •  "),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .horizontalScroll(textScrollState),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge
                )

            }

            Spacer(height = 8.dp)

            // Indicator
            PagerIndicator(size = pagerState.pageCount, currentPage = pagerState.currentPage)
        }
    } else {
        if (showPlaceholder) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(246.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(144.dp)
                                .height(54.dp)
                                .background(placeHolderColor)
                        )
                        Spacer(height = 16.dp)
                        Text(
                            text = "",
                            modifier = Modifier
                                .fillMaxWidth(.8f)
                                .background(placeHolderColor),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                Spacer(height = 8.dp)
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(placeHolderColor)
                )
            }
        }

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
