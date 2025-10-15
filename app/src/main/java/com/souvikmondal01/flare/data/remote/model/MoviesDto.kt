package com.souvikmondal01.flare.data.remote.model

data class MoviesDto(
    val page: Int?,
    val results: List<MovieDto>?,
    val total_pages: Int?,
    val total_results: Int?
)