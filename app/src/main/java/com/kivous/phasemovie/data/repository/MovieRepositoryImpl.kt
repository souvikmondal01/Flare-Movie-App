package com.kivous.phasemovie.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kivous.phasemovie.data.mapper.toMovie
import com.kivous.phasemovie.data.mapper.toMovieCredits
import com.kivous.phasemovie.data.mapper.toMovieDetails
import com.kivous.phasemovie.data.remote.MovieApi
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.MovieDetails
import com.kivous.phasemovie.domain.model.SliderMovie
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits
import com.kivous.phasemovie.domain.repository.MovieRepository
import com.kivous.phasemovie.util.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val firestore: FirebaseFirestore,
) : MovieRepository {
    override suspend fun getMovieList(
        category: String, page: Int
    ): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())

        runCatching {
            movieApi.getMovieList(category, page).results.map { it.toMovie(category) }
        }.onSuccess { movieList ->
            emit(Response.Success(movieList))
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSimilarMovies(
        id: String
    ): Flow<Response<List<Movie>>> = flow {
        try {
            emit(Response.Loading())
            val movieList = movieApi.getSimilarMovies(id = id).results.map {
                it.toMovie("")
            }
            emit(Response.Success(movieList))
        } catch (e: Exception) {
            emit(Response.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSliderMovieList(): Flow<Response<List<SliderMovie>>> = callbackFlow {
        trySend(Response.Loading())
        val listener = firestore.collection("phase_movie").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(Response.Error(error?.message))
                return@addSnapshotListener
            }
            val movieList = snapshot.toObjects(SliderMovie::class.java)
            trySend(Response.Success(movieList))
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getMovieDetails(id: String): Flow<Response<MovieDetails>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.getMovieDetails(id).toMovieDetails()
        }.onSuccess { movieDetails ->
            emit(Response.Success(movieDetails))
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMovieCredits(id: String): Flow<Response<MovieCredits>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.getMovieCredits(id).toMovieCredits()
        }.onSuccess {
            emit(Response.Success(it))
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
        }
    }

}
