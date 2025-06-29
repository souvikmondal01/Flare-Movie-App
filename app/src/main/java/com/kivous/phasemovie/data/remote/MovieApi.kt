package com.kivous.phasemovie.data.remote

import com.kivous.phasemovie.data.remote.model.MoviesDto
import com.kivous.phasemovie.data.remote.model.SocialDto
import com.kivous.phasemovie.data.remote.model.movie_credits.MovieCreditsDto
import com.kivous.phasemovie.data.remote.model.movie_details.MovieDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("region") region: String = "",
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    /**
     * similar
     * recommendations (only 2 pages)
     */
    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String = "day",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("discover/{media_type}")
    suspend fun discoverMedia(
        @Path("media_type") mediaType: String = "movie",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_origin_country") withOriginCountry: String = "IN",
        @Query("with_original_language") withOriginalLanguage: String = "",
        @Query("primary_release_date.gte") primaryReleaseDateGte: String = "",
        @Query("with_keywords") withKeywords: String = "",
        @Query("with_genres") withGenres: String = "",
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDetailsDto

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieCreditsDto

    @GET("movie/{id}/external_ids")
    suspend fun getSocial(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = API_KEY
    ): SocialDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "676501002986df75334b86def9839a75"
    }

}


/**
 * 210024
 * https://api.themoviedb.org/3/movie/574475/credits?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/movie/574475/external_ids?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/movie/574475?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/movie/popular?language=en-US&page=1&api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/movie/574475/similar?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/movie/574475/recommendations?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/trending/movie/day?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/trending/movie/week?api_key=676501002986df75334b86def9839a75
 * https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_origin_country=IN&with_original_language=hi&api_key=676501002986df75334b86def9839a75
 */
