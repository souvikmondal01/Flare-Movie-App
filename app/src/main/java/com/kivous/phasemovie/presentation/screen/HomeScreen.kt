package com.kivous.phasemovie.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kivous.phasemovie.presentation.compoment.ChangeStatusBarColor
import com.kivous.phasemovie.presentation.compoment.ImageSlider
import com.kivous.phasemovie.presentation.compoment.MovieRowSection
import com.kivous.phasemovie.presentation.viewmodel.MovieListViewModel
import com.kivous.phasemovie.util.Category

@Composable
fun HomeScreen(
) {
    val movieListViewModel: MovieListViewModel = hiltViewModel()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val sliderMovieListState = movieListViewModel.sliderMovieListState.collectAsState().value

    val sliderMovieList = sliderMovieListState.data

    val scrollState = rememberScrollState() // home screen scroll state

    // Change Status-Bar Color according scroll state
    ChangeStatusBarColor(scrollState = scrollState.value)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Image Slider
        Box(
            modifier = Modifier
                .height(264.dp)
                .fillMaxWidth()
        ) {
            if (sliderMovieList.isNotEmpty()) {
                ImageSlider(sliderMovieList = sliderMovieList)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MovieRowSection(
            category = Category.NOW_PLAYING,
            movieList = movieListState.nowPlayingMovieList,
            onMoreClick = { category ->
            },
            onMovieClick = { movie ->
            })

        Spacer(modifier = Modifier.height(24.dp))

        MovieRowSection(
            category = Category.POPULAR,
            movieList = movieListState.popularMovieList,
            onMoreClick = { category ->
            },
            onMovieClick = { movie ->
            })

        Spacer(modifier = Modifier.height(24.dp))

        MovieRowSection(
            category = Category.TOP_RATED,
            movieList = movieListState.topRatedMovieList,
            onMoreClick = { category ->
            },
            onMovieClick = { movie ->
            })

        Spacer(modifier = Modifier.height(24.dp))

        MovieRowSection(
            category = Category.UPCOMING,
            movieList = movieListState.upcomingMovieList,
            onMoreClick = { category ->
            },
            onMovieClick = { movie ->
            })

        Spacer(modifier = Modifier.height(64.dp))

    }

}






