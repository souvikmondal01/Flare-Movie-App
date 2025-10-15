package com.souvikmondal01.flare.data.remote.model

data class SliderMovie(
    val id: String? = "",
    val name: String? = "",
    val bgUrl: String? = "",
    val fgUrl: String? = "",
    val genres: List<String>? = emptyList(),
)