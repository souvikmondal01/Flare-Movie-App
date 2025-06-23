package com.kivous.phasemovie.domain.model.movie_credits

data class MovieCredits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)