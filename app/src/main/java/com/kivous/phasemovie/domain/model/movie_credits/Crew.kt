package com.kivous.phasemovie.domain.model.movie_credits

data class Crew(
    val creditId: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val knownForDepartment: String,
    val name: String,
    val profilePath: String
)