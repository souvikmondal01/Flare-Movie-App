package com.souvikmondal01.flare.domain.repository

import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.domain.model.Movie
import com.souvikmondal01.flare.domain.model.Social
import com.souvikmondal01.flare.domain.model.credits.Credits
import com.souvikmondal01.flare.util.Category
import com.souvikmondal01.flare.util.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getSliderMovies(): Flow<Response<List<Movie>>>

    suspend fun getMovies(category: Category, page: Int): Flow<Response<List<Movie>>>

    suspend fun getTrendingMovies(page: Int): Flow<Response<List<Movie>>>

    suspend fun discoverMedia(
        mediaType: MediaType,
        sortBy: String,
        withOriginCountry: String,
        withOriginalLanguage: String,
        primaryReleaseDateGte: String,
        withKeywords: String,
        withGenres: String,
        page: Int,
    ): Flow<Response<List<Movie>>>

    suspend fun getSimilarMedia(
        mediaType: MediaType, id: Int, page: Int
    ): Flow<Response<List<Movie>>>

    suspend fun getMedia(mediaType: MediaType, id: Int): Flow<Response<Movie>>

    suspend fun getCertification(
        mediaType: MediaType, id: Int, region: String = "IN"
    ): Flow<Response<String>>

    suspend fun getCredits(mediaType: MediaType, id: Int): Flow<Response<Credits>>

    suspend fun getSocials(mediaType: MediaType, id: Int): Flow<Response<Social>>

    suspend fun search(query: String, page: Int): Flow<Response<List<Movie>>>

    suspend fun insertMedia(movie: Movie): Flow<Response<Boolean>>

    suspend fun deleteMediaById(id: Int): Flow<Response<Boolean>>

    suspend fun getAllMedia(): Flow<Response<List<Movie>>>

    fun isMediaSaved(id: Int): Flow<Response<Boolean>>

}