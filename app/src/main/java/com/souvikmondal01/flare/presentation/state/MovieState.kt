package com.souvikmondal01.flare.presentation.state

import com.souvikmondal01.flare.domain.model.Movie
import com.souvikmondal01.flare.domain.model.Social
import com.souvikmondal01.flare.domain.model.credits.Credits
import com.souvikmondal01.flare.util.Category

data class MovieState(
    val moviesState: Map<Category, MoviesWrapper> = emptyMap(),

    // Single Movie
    val movieLoadState: LoadState = LoadState.Initial,
    val movie: Movie? = null,
    val movieError: String = "",

    // Media Credits
    val creditsLoadState: LoadState = LoadState.Initial,
    val credits: Credits? = null,
    val creditsError: String = "",

    // Media Certification
    val certificationLoadState: LoadState = LoadState.Initial,
    val certification: String = "N/A",
    val certificationError: String = "",

    // Media Socials
    val socialLoadState: LoadState = LoadState.Initial,
    val social: Social = Social(-1, "", "", "", "", ""),
    val socialError: String = "",

    // Local Database
    val localDBLoadState: LoadState = LoadState.Initial,
    val localDBMovies: List<Movie>? = null,
    val localDBError: String = "",

    val isMediaSaved: Boolean = false
)