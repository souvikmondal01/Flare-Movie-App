package com.souvikmondal01.flare.presentation.state

import com.souvikmondal01.flare.domain.model.Movie

data class MoviesWrapper(
    val loadState: LoadState = LoadState.Initial,
    val movies: List<Movie> = emptyList(),
    val page: Int = 1,
    val error: String = ""
)
