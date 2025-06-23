package com.kivous.phasemovie.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.presentation.compoment.ChangeStatusBarColor
import com.kivous.phasemovie.presentation.compoment.MovieGrid
import com.kivous.phasemovie.presentation.viewmodel.MovieViewModel
import com.kivous.phasemovie.util.Category

@Composable
fun MovieListScreen(
    category: Category,
    onBackClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {},
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    ChangeStatusBarColor()

    val movieListState by movieViewModel.movieListState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Top app-bar start
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.padding(6.dp)
                )
            }
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
        // Top app-bar end

        val (movieList, isLoading) = when (category) {
            Category.NOW_PLAYING -> movieListState.nowPlayingMovieList to movieListState.isLoadingNowPlaying
            Category.POPULAR -> movieListState.popularMovieList to movieListState.isLoadingPopular
            Category.TOP_RATED -> movieListState.topRatedMovieList to movieListState.isLoadingTopRated
            Category.UPCOMING -> movieListState.upcomingMovieList to movieListState.isLoadingUpcoming
            else -> emptyList<Movie>() to false
        }

        MovieGrid(
            movies = movieList,
            isLoading = isLoading,
            onMovieClick = { onMovieClick(it.id) },
            paginate = { movieViewModel.paginate(category) }
        )

    }

}



