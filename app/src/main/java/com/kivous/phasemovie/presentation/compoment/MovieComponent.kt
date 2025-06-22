package com.kivous.phasemovie.presentation.compoment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.util.Category

@Composable
fun MovieRow(
    category: Category,
    movieList: List<Movie>,
    onMoreClick: (Category) -> Unit,
    onMovieClick: (Movie) -> Unit,
    more: Boolean = true
) {
    val lazyListState = rememberLazyListState()

    if (movieList.isNotEmpty()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(start = 16.dp, end = 8.dp)
                    .clickable(indication = null, interactionSource = remember {
                        MutableInteractionSource()
                    }
                        // to disable ripple effect
                    ) {
                        onMoreClick(category)
                    }, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium
                )

                if (more && lazyListState.firstVisibleItemIndex >= 1) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "More",
                    )
                }

            }

            LazyRow(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movieList) { movie ->
                    MovieCard(
                        movie = movie, onMovieCLick = {
                            onMovieClick(movie)
                        })
                }
            }

        }

    }
}

@Composable
fun MovieGrid(
    movies: List<Movie> = emptyList(),
    isLoading: Boolean = false,
    onMovieClick: (Movie) -> Unit = {},
    paginate: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(movies) { index, movie ->
            MovieCard(movie = movie, onMovieCLick = {
                onMovieClick(movie)
            })
            if (index >= movies.size - 1 && !isLoading) {
                paginate()
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onMovieCLick: () -> Unit
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(movie.poster_path).crossfade(true)
            .build(),
        contentDescription = "Poster image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(152.dp)
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = .1f))
            .clickable { onMovieCLick() },
    )

}

