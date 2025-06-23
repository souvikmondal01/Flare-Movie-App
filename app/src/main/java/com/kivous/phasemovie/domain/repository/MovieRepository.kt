package com.kivous.phasemovie.domain.repository

import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.MovieDetails
import com.kivous.phasemovie.domain.model.SliderMovie
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits
import com.kivous.phasemovie.util.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieList(category: String, page: Int): Flow<Response<List<Movie>>>

    suspend fun getSimilarMovies(id: String): Flow<Response<List<Movie>>>

    suspend fun getSliderMovieList(): Flow<Response<List<SliderMovie>>>

    suspend fun getMovieDetails(id: String): Flow<Response<MovieDetails>>

    suspend fun getMovieCredits(id: String): Flow<Response<MovieCredits>>

}