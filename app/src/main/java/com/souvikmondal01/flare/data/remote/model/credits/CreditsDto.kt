package com.souvikmondal01.flare.data.remote.model.credits

data class CreditsDto(
    val cast: List<CastDto>?,
    val crew: List<CrewDto>?,
    val id: Int?
)