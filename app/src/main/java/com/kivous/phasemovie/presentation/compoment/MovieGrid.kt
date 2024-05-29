package com.kivous.phasemovie.presentation.compoment

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.presentation.screen.MovieDetailsScreen

@Composable
fun MovieGrid(
    movies: List<Movie>, isLoading: Boolean, paginate: () -> Unit
) {
    val navigator = LocalNavigator.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), contentPadding = PaddingValues(start = 8.dp, bottom = 24.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            MovieListItem(movie) {
                navigator?.push(MovieDetailsScreen(it))
            }
            if (index >= movies.size - 1 && !isLoading) {
                paginate()
            }
        }
    }
}