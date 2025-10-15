package com.souvikmondal01.flare.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvikmondal01.flare.presentation.compoment.ImageSlider
import com.souvikmondal01.flare.presentation.compoment.MovieCategoryRow
import com.souvikmondal01.flare.presentation.compoment.ShimmerBox
import com.souvikmondal01.flare.presentation.compoment.Spacer
import com.souvikmondal01.flare.presentation.compoment.StatusBarColor
import com.souvikmondal01.flare.presentation.viewmodel.MovieViewModel
import com.souvikmondal01.flare.ui.theme.Golden
import com.souvikmondal01.flare.util.Category

@Composable
fun HomeScreen(
    onMovieClick: (Int, Category) -> Unit = { _, _ -> },
    onMoreClick: (category: Category) -> Unit = {},
    onBackClick: () -> Unit = {},
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        movieViewModel.getSliderMovies()
    }
    val movieState by movieViewModel.movieState.collectAsStateWithLifecycle()
    val sliderMovies = movieState.moviesState[Category.SLIDER]?.movies.orEmpty()

    val lazyListState = rememberLazyListState()

    val scrollOffset =
        lazyListState.firstVisibleItemIndex * 1000 + lazyListState.firstVisibleItemScrollOffset

    val threshold = 200
    StatusBarColor(scrollState = scrollOffset, threshold = threshold)

    var autoScroll by remember { mutableStateOf(true) }

    val categories = remember {
        listOf(
            Category.LATEST_RELEASES_IN_INDIA,
            Category.TRENDING_MOVIES,
            Category.POPULAR_INDIAN_TV_SHOWS,
            Category.NOW_PLAYING,
            Category.TOP_INDIAN_MOVIES,
            Category.TOP_RATED,
            Category.POPULAR,
            Category.POPULAR_TV_SHOWS,
            Category.POPULAR_ANIME,
            Category.POPULAR_ANIMATION_MOVIES,
            Category.POPULAR_ANIMATION_TV_SHOWS,
            Category.THRILLER_HINDI_MOVIES,
            Category.UPCOMING_INDIAN_MOVIES,
            Category.UPCOMING_MOVIES,
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState
        ) {
            item {
                // Image Slider start
                ImageSlider(movies = sliderMovies, autoScroll = autoScroll)
                // Image Slider end
                Spacer(height = 16.dp)
            }
            itemsIndexed(categories) { index, category ->
                MovieCategoryRow(
                    viewModel = movieViewModel,
                    state = movieState,
                    category = category,
                    onMovieClick = { onMovieClick(it, category) },
                    onMoreClick = { onMoreClick(it) })
                Spacer(height = if (index == categories.lastIndex) 96.dp else 24.dp)
            }
        }

        // Back Button start
        AnimatedVisibility(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp),
            visible = scrollOffset < threshold,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = .3f))
                    .clickable { onBackClick() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                )
            }
        }
        // Back Button end

        // Premium Button start
        AnimatedVisibility(
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp),
            visible = scrollOffset < threshold,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
        ) {
            ShimmerBox(
                text = "Premium",
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 2.dp),
                contentColor = Golden,
                onClick = {
                    autoScroll = !autoScroll
                })
        }
        // Premium Button end

    }


}







