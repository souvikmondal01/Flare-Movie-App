package com.kivous.phasemovie.presentation.state

import com.kivous.phasemovie.domain.model.Movie

data class MoviesWrapper(
    val loadState: LoadState = LoadState.Initial,
    val movies: List<Movie> = emptyList(),
    val page: Int = 1,
    val error: String = ""
)
