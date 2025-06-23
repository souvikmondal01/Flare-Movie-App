package com.kivous.phasemovie.domain.model.movie_credits


data class Cast(
    val castId: Int,
    val character: String,
    val creditId: String,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val order: Int,
    val profilePath: String
)