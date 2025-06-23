package com.kivous.phasemovie.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.presentation.compoment.ChangeStatusBarColor
import com.kivous.phasemovie.presentation.compoment.ImageSlider
import com.kivous.phasemovie.presentation.compoment.MovieRow
import com.kivous.phasemovie.presentation.compoment.ShimmerBox
import com.kivous.phasemovie.presentation.compoment.Spacer
import com.kivous.phasemovie.presentation.viewmodel.MovieViewModel
import com.kivous.phasemovie.ui.theme.Golden
import com.kivous.phasemovie.util.Category

@Composable
fun HomeScreen(
    onMovieClick: (Movie) -> Unit = {},
    onBackClick: () -> Unit = {},
    onMoreClick: (Category) -> Unit = {},
    movieViewModel: MovieViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        movieViewModel.getSliderMovieList()
        movieViewModel.getNowPlayingMovieList()
        movieViewModel.getPopularMovieList()
        movieViewModel.getTopRatedMovieList()
        movieViewModel.getUpcomingMovieList()
    }

    val movieListState by movieViewModel.movieListState.collectAsStateWithLifecycle()

    val movieSections = listOf(
        Category.NOW_PLAYING to movieListState.nowPlayingMovieList,
        Category.POPULAR to movieListState.popularMovieList,
        Category.TOP_RATED to movieListState.topRatedMovieList,
        Category.UPCOMING to movieListState.upcomingMovieList
    )

    val homeScreenScrollState = rememberScrollState()

    // Change Status-Bar Color according scroll state
    ChangeStatusBarColor(scrollState = homeScreenScrollState.value)

    var autoScroll by remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(homeScreenScrollState)
                .navigationBarsPadding()
        ) {
            // Image Slider start
            if (movieListState.sliderMovieList.isNotEmpty()) {
                ImageSlider(
                    sliderMovieList = movieListState.sliderMovieList,
                    autoScroll = autoScroll
                )
            }
            // Image Slider end

            Spacer(height = 16.dp)

            movieSections.forEachIndexed { index, (category, movieList) ->
                MovieRow(
                    category = category,
                    movieList = movieList,
                    onMoreClick = {
                        onMoreClick(it)
                    },
                    onMovieClick = {
                        onMovieClick(it)
                    })
                Spacer(height = if (index == movieSections.lastIndex) 64.dp else 24.dp)
            }

        }

        // Back Button start
        AnimatedVisibility(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp),
            visible = homeScreenScrollState.value < 200,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = .3f))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
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
            visible = homeScreenScrollState.value < 200,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
        ) {
            ShimmerBox(
                text = "Premium",
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 2.dp),
                contentColor = Golden,
                onClick = {
                    autoScroll = !autoScroll
                }
            )
        }
        // Premium Button end

    }


}






