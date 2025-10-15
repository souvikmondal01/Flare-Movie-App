package com.souvikmondal01.flare.presentation.screen

import androidx.navigation3.runtime.NavKey
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.util.Category
import kotlinx.serialization.Serializable

interface Screen {
    @Serializable
    data object HomeScreen : NavKey

    @Serializable
    data object SearchScreen : NavKey

    @Serializable
    data object FavouriteScreen : NavKey

    @Serializable
    data class MovieDetailScreen(val id: Int, val mediaType: MediaType) : NavKey

    @Serializable
    data class MovieGridScreen(val category: Category) : NavKey

    @Serializable
    data class CreditsScreen(val id: Int, val mediaType: MediaType) : NavKey

}