package com.kivous.phasemovie.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kivous.phasemovie.presentation.compoment.ChangeStatusBarColor
import com.kivous.phasemovie.presentation.compoment.MovieGrid
import com.kivous.phasemovie.presentation.viewmodel.MovieListViewModel
import com.kivous.phasemovie.ui.theme.NunitoBold
import com.kivous.phasemovie.util.Category

@Composable
fun MovieListScreen(category: Category) {

    val movieListViewModel: MovieListViewModel = hiltViewModel()
    val movieListState = movieListViewModel.movieListState.collectAsState().value

    ChangeStatusBarColor(Color.Black)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box( // <-action bar
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Row {
                Card( // <- back arrow
                    modifier = Modifier,
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(Color.Transparent),
                    onClick = {
                    }) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back arrow",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = category.title,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = NunitoBold
                )
            }
        }

        when (category) {
            Category.NOW_PLAYING -> {
                MovieGrid(
                    movieListState.nowPlayingMovieList,
                    movieListState.isLoadingNowPlaying,
                ) {
                    movieListViewModel.paginate(category)
                }
            }

            Category.POPULAR -> {
                MovieGrid(
                    movieListState.popularMovieList,
                    movieListState.isLoadingPopular,
                ) {
                    movieListViewModel.paginate(category)
                }
            }

            Category.TOP_RATED -> {
                MovieGrid(
                    movieListState.topRatedMovieList,
                    movieListState.isLoadingTopRated,
                ) {
                    movieListViewModel.paginate(category)
                }
            }

            Category.UPCOMING -> {
                MovieGrid(
                    movieListState.upcomingMovieList,
                    movieListState.isLoadingUpcoming,
                ) {
                    movieListViewModel.paginate(category)
                }
            }

            else -> {}
        }

    }

}



