package com.kivous.phasemovie.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kivous.phasemovie.data.mapper.toMovie
import com.kivous.phasemovie.data.mapper.toMovieCredits
import com.kivous.phasemovie.data.mapper.toSocial
import com.kivous.phasemovie.data.remote.MovieApi
import com.kivous.phasemovie.data.remote.model.SliderMovie
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.Social
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits
import com.kivous.phasemovie.domain.repository.MovieRepository
import com.kivous.phasemovie.util.Category
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

    override suspend fun getMovies(category: Category, page: Int): Flow<Response<List<Movie>>> =
        flow {
            emit(Response.Loading())
            runCatching {
                movieApi.getMovies(
                    category.category, page, category.region
                ).results.map { it.toMovie() }
            }.onSuccess { movies ->
                emit(Response.Success(movies))
            }.onFailure { e ->
                emit(Response.Error(e.message ?: "Unknown error occurred"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getSimilarMovies(
        id: String, page: Int
    ): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.getSimilarMovies(id = id, page = page).results.map {
                it.toMovie()
            }
        }.onSuccess { movies ->
            emit(Response.Success(movies))
        }.onFailure { e ->
            emit(Response.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getTrendingMovies(page: Int): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.getTrendingMovies(page = page).results.map {
                it.toMovie()
            }
        }.onSuccess {
            emit(Response.Success(it))
        }.onFailure {
            emit(Response.Error(it.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun discoverMedia(
        mediaType: String,
        page: Int,
        sortBy: String,
        withOriginCountry: String,
        withOriginalLanguage: String,
        primaryReleaseDateGte: String,
        withKeywords: String,
        withGenres: String
    ): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.discoverMedia(
                mediaType = mediaType,
                page = page,
                sortBy = sortBy,
                withOriginCountry = withOriginCountry,
                withOriginalLanguage = withOriginalLanguage,
                primaryReleaseDateGte = primaryReleaseDateGte,
                withKeywords = withKeywords,
                withGenres = withGenres
            ).results.map {
                it.toMovie()
            }

        }.onSuccess {
            emit(Response.Success(it))
        }.onFailure {
            emit(Response.Error(it.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSliderMovies(): Flow<Response<List<Movie>>> = callbackFlow {
        trySend(Response.Loading())
        val listener = firestore.collection("phase_movie").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(Response.Error(error?.message))
                return@addSnapshotListener
            }
            val movieList = snapshot.toObjects(SliderMovie::class.java)

            val movies = movieList.map {
                it.toMovie()
            }

            trySend(Response.Success(movies))
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getMovie(id: String): Flow<Response<Movie>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.getMovie(id).toMovie()
        }.onSuccess { movie ->
            emit(Response.Success(movie))
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

    override suspend fun getSocial(id: String): Flow<Response<Social>> = flow {
        emit(Response.Loading())
        runCatching {
            movieApi.getSocial(id).toSocial()
        }.onSuccess {
            emit(Response.Success(it))
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
        }
    }

}
