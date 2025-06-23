package com.kivous.phasemovie.presentation.state

import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.MovieDetails
import com.kivous.phasemovie.domain.model.SliderMovie
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits

data class MovieState(
    // Now Playing Movie
    val isLoadingNowPlaying: Boolean = false,
    val nowPlayingMovieListPage: Int = 1,
    val nowPlayingMovieList: List<Movie> = emptyList(),
    val nowPlayingMovieError: String = "",

    // Popular Movie
    val isLoadingPopular: Boolean = false,
    val popularMovieListPage: Int = 1,
    val popularMovieList: List<Movie> = emptyList(),
    val popularMovieError: String = "",

    // Top Rated Movie
    val isLoadingTopRated: Boolean = false,
    val topRatedMovieListPage: Int = 1,
    val topRatedMovieList: List<Movie> = emptyList(),
    val topRatedMovieError: String = "",

    // Upcoming Movie
    val isLoadingUpcoming: Boolean = false,
    val upcomingMovieListPage: Int = 1,
    val upcomingMovieList: List<Movie> = emptyList(),
    val upcomingMovieError: String = "",

    // Similar Movie
    val isLoadingSimilar: Boolean = false,
    val similarMovieList: List<Movie> = emptyList(),
    val similarMovieError: String = "",

    // Slider Movie
    val isLoadingSliderMovie: Boolean = false,
    val sliderMovieList: List<SliderMovie> = emptyList(),
    val sliderMovieError: String = "",

    // Single Movie Details
    val isLoadingMovieDetails: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val movieDetailsError: String = "",

    // Movie Credits
    val isLoadingMovieCredits: Boolean = false,
    val movieCredits: MovieCredits? = null,
    val movieCreditsError: String = ""
)