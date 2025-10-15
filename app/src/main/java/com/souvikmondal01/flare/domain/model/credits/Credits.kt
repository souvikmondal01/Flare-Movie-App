package com.souvikmondal01.flare.domain.model.credits

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)