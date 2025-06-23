package com.kivous.phasemovie.data.remote.model

data class MovieListDto(
    val page: Int?,
    val results: List<MovieDto>,
    val total_pages: Int?,
    val total_results: Int?
)