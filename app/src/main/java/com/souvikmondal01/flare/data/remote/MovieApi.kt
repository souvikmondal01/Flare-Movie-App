package com.souvikmondal01.flare.data.remote

import com.souvikmondal01.flare.data.remote.model.MoviesDto
import com.souvikmondal01.flare.data.remote.model.SocialDto
import com.souvikmondal01.flare.data.remote.model.certification.movie.MovieCertificationDto
import com.souvikmondal01.flare.data.remote.model.certification.tv.TvCertificationDto
import com.souvikmondal01.flare.data.remote.model.credits.CreditsDto
import com.souvikmondal01.flare.data.remote.model.movie_details.MovieDetailsDto
import com.souvikmondal01.flare.data.remote.model.tv_details.TvDetailsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    /**
     * category =  now_playing, popular, top_rated, upcoming
     */
    @GET("movie/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("region") region: String,
        @Query("page") page: Int,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MoviesDto

    /**
     * time_window = day, week
     */
    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String = "day",
        @Query("page") page: Int = 1,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MoviesDto

    /**
     * media_type = movie, tv
     */
    @GET("discover/{media_type}")
    suspend fun discoverMedia(
        @Path("media_type") mediaType: String,
        @Query("sort_by") sortBy: String,
        @Query("with_origin_country") withOriginCountry: String,
        @Query("with_original_language") withOriginalLanguage: String,
        @Query("primary_release_date.gte") primaryReleaseDateGte: String,
        @Query("with_keywords") withKeywords: String,
        @Query("with_genres") withGenres: String,
        @Query("page") page: Int = 1,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MoviesDto

    /**
     * similar
     * or
     * recommendations (only 2 pages)
     */
    @GET("{media_type}/{id}/similar")
    suspend fun getSimilarMedia(
        @Path("media_type") mediaType: String,
        @Path("id") id: Int,
        @Query("page") page: Int = 1,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MoviesDto

    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") id: Int, @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MovieDetailsDto

    @GET("tv/{id}")
    suspend fun getTv(
        @Path("id") id: Int, @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): TvDetailsDto

    @GET("movie/{id}/release_dates")
    suspend fun getMovieCertification(
        @Path("id") id: Int, @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MovieCertificationDto

    @GET("tv/{id}/content_ratings")
    suspend fun getTvCertification(
        @Path("id") id: Int, @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): TvCertificationDto

    @GET("{media_type}/{id}/credits")
    suspend fun getCredits(
        @Path("media_type") mediaType: String,
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): CreditsDto

    @GET("{media_type}/{id}/external_ids")
    suspend fun getSocial(
        @Path("media_type") mediaType: String,
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): SocialDto

    @GET("search/multi")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
    ): MoviesDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NzY1MDEwMDI5ODZkZjc1MzM0Yjg2ZGVmOTgzOWE3NSIsIm5iZiI6MTYzMzAwNzk1MS43MTIsInN1YiI6IjYxNTViOTRmZGNiNmEzMDA0MzRiZWExYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.pAYRLFTKWf-xguf8GoxwnYFUXhzhxy4u-AEwWj7ohJI"
    }

}

/**
 * @Header("Authorization") authHeader: String = "Bearer $API_TOKEN"
 * or
 * @Query("api_key") apiKey: String = API_KEY
 */

/**
 * const val API_KEY = "676501002986df75334b86def9839a75"
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
