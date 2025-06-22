package com.kivous.phasemovie.data.remote

import com.kivous.phasemovie.data.remote.model.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto

    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto



    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "676501002986df75334b86def9839a75"
    }

}


/**
 *  https://api.themoviedb.org/3/movie/popular?language=en-US&page=1&api_key=676501002986df75334b86def9839a75
 * */
