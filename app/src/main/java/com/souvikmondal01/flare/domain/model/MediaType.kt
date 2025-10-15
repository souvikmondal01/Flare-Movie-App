package com.souvikmondal01.flare.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MediaType(val type: String) {
    @Serializable
    @SerialName("movie")
    data object Movie : MediaType("movie")

    @Serializable
    @SerialName("tv")
    data object Tv : MediaType("tv")
}


