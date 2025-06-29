package com.kivous.phasemovie.presentation.state

import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.Social
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits
import com.kivous.phasemovie.util.Category

data class MovieState(
    val moviesState: Map<Category, MoviesWrapper> = emptyMap(),

    // Single Movie
    val movieLoadState: LoadState = LoadState.Initial,
    val movie: Movie? = null,
    val movieError: String = "",

    // Movie Credits
    val isLoadingMovieCredits: Boolean = false,
    val movieCredits: MovieCredits? = null,
    val movieCreditsError: String = "",

    // Social
    val isLoadingSocial: Boolean = false,
    val social: Social? = null,
    val socialError: String = ""

)