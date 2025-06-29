package com.kivous.phasemovie.domain.repository

import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.Social
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits
import com.kivous.phasemovie.util.Category
import com.kivous.phasemovie.util.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovies(category: Category, page: Int): Flow<Response<List<Movie>>>

    suspend fun getSimilarMovies(id: String, page: Int): Flow<Response<List<Movie>>>

    suspend fun getTrendingMovies(page: Int): Flow<Response<List<Movie>>>

    suspend fun discoverMedia(
        mediaType: String = "",
        page: Int = 1,
        sortBy: String = "",
        withOriginCountry: String = "",
        withOriginalLanguage: String = "",
        primaryReleaseDateGte: String = "",
        withKeywords: String = "",
        withGenres: String = "",
    ): Flow<Response<List<Movie>>>

    suspend fun getSliderMovies(): Flow<Response<List<Movie>>>

    suspend fun getMovie(id: String): Flow<Response<Movie>>

    suspend fun getMovieCredits(id: String): Flow<Response<MovieCredits>>

    suspend fun getSocial(id: String): Flow<Response<Social>>

}