package com.kivous.phasemovie.data.remote.model.movie_credits

data class MovieCreditsDto(
    val cast: List<CastDto>?,
    val crew: List<CrewDto>?,
    val id: Int?
)