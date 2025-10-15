package com.souvikmondal01.flare.domain.model

import com.souvikmondal01.flare.domain.model.credits.Cast

data class Movie(
    val adult: Boolean,
    val backdropPath: String,
    val budget: Int,
    val genres: List<String>,
    val homepage: String,
    val id: Int,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val firstAirDate: String,
    val name: String,
    val certification: String,
    val topCast: List<Cast>,
    val social: Social,
    val mediaType: String,
)
