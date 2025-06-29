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
import com.kivous.phasemovie.presentation.compoment.ImageSlider
import com.kivous.phasemovie.presentation.compoment.MovieCategoryRow
import com.kivous.phasemovie.presentation.compoment.ShimmerBox
import com.kivous.phasemovie.presentation.compoment.Spacer
import com.kivous.phasemovie.presentation.compoment.StatusBarColor
import com.kivous.phasemovie.presentation.viewmodel.MovieViewModel
import com.kivous.phasemovie.ui.theme.Golden
import com.kivous.phasemovie.util.Category
import com.kivous.phasemovie.util.Common

@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit = {},
    onMoreClick: (category: Category) -> Unit = {},
    onBackClick: () -> Unit = {},
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        movieViewModel.getSliderMovies()
    }
    val movieState by movieViewModel.movieState.collectAsStateWithLifecycle()
    val sliderMovies = movieState.moviesState[Category.SLIDER]?.movies.orEmpty()

    val scrollState = rememberScrollState()

    // Change Status-Bar Color according scroll state
    StatusBarColor(scrollState = scrollState.value)

    var autoScroll by remember { mutableStateOf(true) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
        ) {
            // Image Slider start
            ImageSlider(movies = sliderMovies, autoScroll = autoScroll)
            // Image Slider end
            Spacer(height = 16.dp)

//          Latest Releases in India
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.LATEST_RELEASES_IN_INDIA,
                onMovieClick = { onMovieClick(it) },
                onMoreClick = { onMoreClick(it) })
            Spacer(height = 24.dp)

//          Trending Movies Today
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.TRENDING_MOVIES,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                })
            Spacer(height = 24.dp)

//          Popular Indian TV Shows
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.POPULAR_INDIAN_TV_SHOWS,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
                isCardWide = true
            )
            Spacer(height = 24.dp)

//          Now Playing in Theaters
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.NOW_PLAYING,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                })
            Spacer(height = 24.dp)

//          Top 20 Indian Movies
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.TOP_INDIAN_MOVIES,
                onMovieClick = {
                    onMovieClick(it)
                },
                showMore = false,
                showLanguageFilter = true,
                languages = Common.indianLanguages
            )
            Spacer(height = 24.dp)

//          Top Rated Worldwide
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.TOP_RATED,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                })
            Spacer(height = 24.dp)

//          Popular Movies
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.POPULAR,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                })
            Spacer(height = 24.dp)

//          Popular TV Shows
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.POPULAR_TV_SHOWS,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
                isCardWide = true
            )
            Spacer(height = 24.dp)

//          Popular Anime
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.POPULAR_ANIME,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
            )
            Spacer(height = 24.dp)

//          Top Animated Movies
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.POPULAR_ANIMATION_MOVIES,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
            )
            Spacer(height = 24.dp)

//          popular_animation_tv_shows
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.POPULAR_ANIMATION_TV_SHOWS,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
            )
            Spacer(height = 24.dp)

//          Must-Watch Hindi Thrillers
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.THRILLER_HINDI_MOVIES,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
            )
            Spacer(height = 24.dp)

//          Upcoming Indian Movies
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.UPCOMING_INDIAN_MOVIES,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
            )
            Spacer(height = 24.dp)

//          Upcoming Indian Movies
            MovieCategoryRow(
                viewModel = movieViewModel,
                state = movieState,
                category = Category.UPCOMING_MOVIES,
                onMovieClick = {
                    onMovieClick(it)
                },
                onMoreClick = {
                    onMoreClick(it)
                },
            )

            Spacer(height = 96.dp)

        }

        // Back Button start
        AnimatedVisibility(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp),
            visible = scrollState.value < 200,
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
            visible = scrollState.value < 200,
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






