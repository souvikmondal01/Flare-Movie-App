package com.souvikmondal01.flare.data.remote.model.tv_details

data class TvDetailsDto(
    val adult: Boolean?,
    val backdrop_path: String?,
    val first_air_date: String?,
    val genres: List<GenreDto>?,
    val homepage: String?,
    val id: Int?,
    val name: String?,
    val original_language: String?,
    val original_name: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val status: String?,
    val tagline: String?,
    val type: String?,
    val vote_average: Double?,
    val vote_count: Int?
)