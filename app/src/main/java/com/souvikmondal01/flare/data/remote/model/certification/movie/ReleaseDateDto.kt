package com.souvikmondal01.flare.data.remote.model.certification.movie

data class ReleaseDateDto(
    val certification: String?,
    val descriptors: List<String>?,
    val iso_639_1: String?,
    val note: String?,
    val release_date: String?,
    val type: Int?
)