package com.kivous.phasemovie.presentation.screen

import androidx.navigation3.runtime.NavKey
import com.kivous.phasemovie.util.Category
import kotlinx.serialization.Serializable

interface Screen {
    @Serializable
    data object HomeScreen : NavKey

    @Serializable
    data class MovieDetailScreen(val movieId: Int) : NavKey

    @Serializable
    data class MovieListScreen(val category: Category) : NavKey

}