package com.kivous.phasemovie.presentation.compoment

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.presentation.state.LoadState
import com.kivous.phasemovie.presentation.state.MovieState
import com.kivous.phasemovie.presentation.viewmodel.MovieViewModel
import com.kivous.phasemovie.util.Category

@Composable
fun MovieCategoryRow(
    category: Category,
    viewModel: MovieViewModel,
    state: MovieState,
    title: String = category.title,
    showMore: Boolean = true,
    onMovieClick: (Int) -> Unit = {},
    onMoreClick: (Category) -> Unit = {},
    movieId: Int = 0, // Required when fetching similar movies
    isCardWide: Boolean = category.isCardWide,
    showLanguageFilter: Boolean = category.showLanguageFilter,
    languages: List<Pair<String, String>> = emptyList(),
) {
    val moviesState = state.moviesState[category]
    val movies = moviesState?.movies.orEmpty()

    var languageCode by rememberSaveable { mutableStateOf(languages.firstOrNull()?.first.orEmpty()) }
    val isLoading = moviesState?.loadState == LoadState.Loading
    val lazyListState = rememberLazyListState()

    LaunchedEffect(category, languageCode) {
        when (category) {
            Category.LATEST_RELEASES_IN_INDIA,
            Category.NOW_PLAYING,
            Category.POPULAR,
            Category.TOP_RATED -> viewModel.getMovies(category)

            Category.TRENDING_MOVIES -> viewModel.getTrendingMovies()

            Category.TOP_INDIAN_MOVIES -> {
                viewModel.getDiscoverMedia(
                    category = category,
                    language = languageCode,
                    usePage = false,
                    append = false
                )
            }

            Category.SIMILAR -> viewModel.getSimilarMovies(movieId.toString())

            else -> viewModel.getDiscoverMedia(category)
        }

    }

    MovieRow(
        title = title,
        movies = movies,
        onMoreClick = {
            onMoreClick(category)
        },
        onMovieClick = {
            onMovieClick(it)
        },
        showMore = showMore,
        paginate = {
            viewModel.paginate(category, movieId.toString())
        },
        isCardWide = isCardWide,
        showLanguageFilter = showLanguageFilter,
        languages = languages,
        languageCode = languageCode,
        getLanguageCode = {
            languageCode = it
        },
        isLoading = isLoading,
        lazyListState = lazyListState
    )
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun MovieRow(
    movies: List<Movie> = emptyList(),
    title: String = "Movie Row",
    onMoreClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {},
    paginate: () -> Unit = {},
    showMore: Boolean = true,
    isCardWide: Boolean = false,
    showPlaceholder: Boolean = true,
    placeHolderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
    lazyListState: LazyListState = rememberLazyListState(),
    showLanguageFilter: Boolean = false,
    languages: List<Pair<String, String>> = emptyList(),
    languageCode: String = "",
    getLanguageCode: (String) -> Unit = {},
    isLoading: Boolean = false,
) {
    val showPlaceholder = showPlaceholder && movies.isEmpty()
    val cardHeight = if (isCardWide) 120.dp else 152.dp
    val cardWidth = if (isCardWide) 224.dp else 104.dp
    val cardCornerRadius = 6.dp

    Column {
//      Row title start
        Row(
            modifier = Modifier
                .fillMaxWidth(if (showPlaceholder) .9f else 1f)
                .padding(vertical = 8.dp)
                .padding(start = 16.dp, end = 8.dp)
                .clip(RoundedCornerShape(if (showPlaceholder) 6.dp else 0.dp))
                .background(if (showPlaceholder) placeHolderColor else Color.Transparent)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                    // to disable ripple effect
                ) {
                    onMoreClick()
                },
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
                text = if (showPlaceholder) "" else title,
                style = MaterialTheme.typography.titleMedium
            )
            AnimatedVisibility(visible = showMore && lazyListState.firstVisibleItemIndex >= 1) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "More"
                )
            }
        }
//      Row title end

        if (!showLanguageFilter) {
            LazyRow(
                state = lazyListState,
                userScrollEnabled = !showPlaceholder,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!showPlaceholder) {
                    itemsIndexed(movies, key = { _, movie -> movie.id }) { index, movie ->
                        val imagePath = if (isCardWide) movie.backdropPath else movie.posterPath
                        ImageCard(
                            imagePath = imagePath,
                            onClick = { onMovieClick(movie.id) },
                            height = cardHeight,
                            width = cardWidth,
                            contentDescription = movie.title.ifEmpty { movie.name }
                        )
                        if (index >= movies.size - 1) {
                            paginate()
                        }
                    }

                } else {
                    items(4) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(cardCornerRadius))
                                .background(placeHolderColor)
                                .height(cardHeight)
                                .width(cardWidth)
                        )
                    }
                }
            }
        } else {
            var languageCode by rememberSaveable { mutableStateOf(languageCode) }
            Column {
//              Row Languages start
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = !showPlaceholder
                ) {
                    if (!showPlaceholder) {
                        items(languages) {
                            val selected = it.first == languageCode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = if (selected) .15f else .1f))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = if (selected) .9f else .1f
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        getLanguageCode(it.first)
                                        languageCode = it.first
                                    }) {
                                Text(
                                    text = it.second,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    } else {
                        items(5) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(placeHolderColor)
                            ) {
                                Text(
                                    text = "               ",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
//              Row Languages end

//              Row Movies start
                LazyRow(
                    state = lazyListState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = !showPlaceholder
                ) {
                    if (!showPlaceholder) {
                        itemsIndexed(movies) { index, movie ->
                            Box {
                                ImageCard(
                                    modifier = Modifier.padding(start = 16.dp),
                                    imagePath = if (isLoading) "" else if (isCardWide) movie.backdropPath else movie.posterPath,
                                    onClick = { onMovieClick(movie.id) },
                                    height = cardHeight,
                                    width = cardWidth,
                                    contentDescription = if (isLoading) "" else movie.title.ifEmpty { movie.name }
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .offset(y = (26).dp)
                                ) {
                                    val text = if (isLoading) "" else "${index + 1}"
                                    val shadowColor = MaterialTheme.colorScheme.background
                                    val spread = .4.dp // How far the "glow" extends

                                    // Draw shadow in 8 directions
                                    for (dx in listOf(-spread, 0.dp, spread)) {
                                        for (dy in listOf(-spread, 0.dp, spread)) {
                                            if (dx != 0.dp || dy != 0.dp) {
                                                Text(
                                                    text = text,
                                                    style = MaterialTheme.typography.displayLarge.copy(
                                                        color = shadowColor
                                                    ),
                                                    modifier = Modifier.offset(x = dx, y = dy)
                                                )
                                            }
                                        }
                                    }

                                    // Main text on top
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.displayLarge.copy(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.onBackground,
                                                    MaterialTheme.colorScheme.onBackground,
                                                    MaterialTheme.colorScheme.background,
                                                )
                                            )
                                        )
                                    )
                                }

                            }
                        }
                    } else {
                        items(4) {
                            Box {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .clip(RoundedCornerShape(cardCornerRadius))
                                        .background(placeHolderColor)
                                        .height(cardHeight)
                                        .width(cardWidth)
                                )
                            }
                        }
                    }
                }
//              Row Movies end
            }
        }


    }
}


@Composable
fun MovieGrid(
    movies: List<Movie> = emptyList(),
    onMovieClick: (Int) -> Unit = {},
    paginate: () -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(movies) { index, movie ->
            ImageCard(
                imagePath = movie.posterPath, onClick = {
                    onMovieClick(movie.id)
                }, contentDescription = movie.title.ifEmpty { movie.name }
            )
            if (index >= movies.size - 1) {
                paginate()
            }
        }
    }
}
