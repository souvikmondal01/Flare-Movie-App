package com.souvikmondal01.flare.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.souvikmondal01.flare.data.local.MediaDao
import com.souvikmondal01.flare.data.mapper.toMedia
import com.souvikmondal01.flare.data.mapper.toMovie
import com.souvikmondal01.flare.data.mapper.toMovieCredits
import com.souvikmondal01.flare.data.mapper.toSocial
import com.souvikmondal01.flare.data.remote.MovieApi
import com.souvikmondal01.flare.data.remote.model.SliderMovie
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.domain.model.Movie
import com.souvikmondal01.flare.domain.model.Social
import com.souvikmondal01.flare.domain.model.credits.Credits
import com.souvikmondal01.flare.domain.repository.MovieRepository
import com.souvikmondal01.flare.performance.PerformanceTracker
import com.souvikmondal01.flare.util.Category
import com.souvikmondal01.flare.util.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val firestore: FirebaseFirestore,
    private val pt: PerformanceTracker,
    private val dao: MediaDao
) : MovieRepository {

    override suspend fun getSliderMovies(): Flow<Response<List<Movie>>> = callbackFlow {
        val trace = pt.startTrace("load_slider_movies")
        trySend(Response.Loading())
        val listener = firestore.collection("phase_movie").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(Response.Error(error?.message))
                pt.stopTrace(trace)
                return@addSnapshotListener
            }
            val movies = snapshot.toObjects(SliderMovie::class.java).map {
                it.toMovie()
            }
            trySend(Response.Success(movies))
            pt.stopTrace(trace)
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getMovies(category: Category, page: Int): Flow<Response<List<Movie>>> =
        flow {
            val trace = pt.startTrace("load_get_movies")
            emit(Response.Loading())
            runCatching {
                movieApi.getMovies(
                    category = category.category, region = category.region, page = page
                ).results?.map { it.toMovie() }.orEmpty()
            }.onSuccess { movies ->
                emit(Response.Success(movies))
                pt.stopTrace(trace)
            }.onFailure { e ->
                emit(Response.Error(e.message ?: "Unknown error occurred"))
                pt.stopTrace(trace)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getTrendingMovies(page: Int): Flow<Response<List<Movie>>> = flow {
        val trace = pt.startTrace("load_get_trending_movies")
        emit(Response.Loading())
        runCatching {
            movieApi.getTrendingMovies(page = page).results?.map {
                it.toMovie()
            }.orEmpty()
        }.onSuccess {
            emit(Response.Success(it))
            pt.stopTrace(trace)
        }.onFailure {
            emit(Response.Error(it.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun discoverMedia(
        mediaType: MediaType,
        sortBy: String,
        withOriginCountry: String,
        withOriginalLanguage: String,
        primaryReleaseDateGte: String,
        withKeywords: String,
        withGenres: String,
        page: Int,
    ): Flow<Response<List<Movie>>> = flow {
        val trace = pt.startTrace("load_discover_media")
        emit(Response.Loading())
        runCatching {
            movieApi.discoverMedia(
                mediaType = mediaType.type,
                sortBy = sortBy,
                withOriginCountry = withOriginCountry,
                withOriginalLanguage = withOriginalLanguage,
                primaryReleaseDateGte = primaryReleaseDateGte,
                withKeywords = withKeywords,
                withGenres = withGenres,
                page = page,
            ).results?.map {
                it.toMovie()
            }.orEmpty()

        }.onSuccess {
            emit(Response.Success(it))
            pt.stopTrace(trace)
        }.onFailure {
            emit(Response.Error(it.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSimilarMedia(
        mediaType: MediaType, id: Int, page: Int
    ): Flow<Response<List<Movie>>> = flow {
        val trace = pt.startTrace("load_get_similar_media")
        emit(Response.Loading())
        runCatching {
            movieApi.getSimilarMedia(
                mediaType = mediaType.type, id = id, page = page
            ).results?.map {
                it.toMovie()
            }.orEmpty()
        }.onSuccess { movies ->
            emit(Response.Success(movies))
            pt.stopTrace(trace)
        }.onFailure { e ->
            emit(Response.Error(e.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMedia(mediaType: MediaType, id: Int): Flow<Response<Movie>> = flow {
        val trace = pt.startTrace("load_get_media")
        emit(Response.Loading())
        runCatching {
            when (mediaType) {
                is MediaType.Movie -> movieApi.getMovie(id).toMovie()

                is MediaType.Tv -> movieApi.getTv(id).toMovie()
            }
        }.onSuccess { movie ->
            emit(Response.Success(movie))
            pt.stopTrace(trace)
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCertification(
        mediaType: MediaType, id: Int, region: String
    ): Flow<Response<String>> = flow {
        val trace = pt.startTrace("load_get_certification")
        emit(Response.Loading())
        runCatching {
            when (mediaType) {
                is MediaType.Movie -> movieApi.getMovieCertification(id).results?.firstOrNull { it.iso_3166_1 == region }?.release_dates?.firstOrNull()?.certification.orEmpty()
                    .ifEmpty { "N/A" }

                is MediaType.Tv -> movieApi.getTvCertification(id).results?.firstOrNull { it.iso_3166_1 == region }?.rating.orEmpty()
                    .ifEmpty { "N/A" }
            }
        }.onSuccess { certification ->
            emit(Response.Success(certification))
            pt.stopTrace(trace)
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCredits(mediaType: MediaType, id: Int): Flow<Response<Credits>> = flow {
        val trace = pt.startTrace("load_get_credits")
        emit(Response.Loading())
        runCatching {
            movieApi.getCredits(mediaType = mediaType.type, id = id).toMovieCredits()
        }.onSuccess { credits ->
            emit(Response.Success(credits))
            pt.stopTrace(trace)
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error occurred"))
            pt.stopTrace(trace)
        }
    }

    override suspend fun getSocials(mediaType: MediaType, id: Int): Flow<Response<Social>> = flow {
        val trace = pt.startTrace("load_get_socials")
        emit(Response.Loading())
        runCatching {
            movieApi.getSocial(mediaType = mediaType.type, id = id).toSocial()
        }.onSuccess {
            emit(Response.Success(it))
            pt.stopTrace(trace)
        }.onFailure {
            emit(Response.Error(it.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun search(
        query: String, page: Int
    ): Flow<Response<List<Movie>>> = flow {
        val trace = pt.startTrace("load_search")
        emit(Response.Loading())
        runCatching {
            movieApi.search(
                query = query, page = page
            ).results?.filter { it.media_type == "movie" || it.media_type == "tv" }
                ?.map { it.toMovie() }.orEmpty()
        }.onSuccess { movies ->
            emit(Response.Success(movies))
            pt.stopTrace(trace)
        }.onFailure {
            emit(Response.Error(it.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertMedia(movie: Movie): Flow<Response<Boolean>> = flow {
        val trace = pt.startTrace("load_insert_media")
        emit(Response.Loading())
        runCatching {
            dao.insertMedia(movie.toMedia())
        }.onSuccess {
            emit(Response.Success(true))
            pt.stopTrace(trace)
        }.onFailure {
            emit(Response.Error(it.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteMediaById(id: Int): Flow<Response<Boolean>> = flow {
        val trace = pt.startTrace("load_delete_media_by_id")
        emit(Response.Loading())
        runCatching {
            dao.deleteMediaById(id)
        }.onSuccess {
            emit(Response.Success(true))
            pt.stopTrace(trace)
        }.onFailure {
            emit(Response.Error(it.message))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllMedia(): Flow<Response<List<Movie>>> = flow {
        val trace = pt.startTrace("load_get_all_media")
        emit(Response.Loading())
        runCatching {
            dao.getAllMedia().map { list -> list.map { it.toMovie() } }.collect { movies ->
                emit(Response.Success(movies))
                pt.stopTrace(trace)
            }
        }.onFailure { e ->
            emit(Response.Error(e.message ?: "Unknown error"))
            pt.stopTrace(trace)
        }
    }

    override fun isMediaSaved(id: Int): Flow<Response<Boolean>> = flow {
        val trace = pt.startTrace("load_is_media_saved")
        emit(Response.Loading())
        runCatching {
            dao.isMediaSaved(id).collect {
                emit(Response.Success(it))
                pt.stopTrace(trace)
            }
        }.onFailure {
            emit(Response.Error(it.message ?: "Unknown error"))
            pt.stopTrace(trace)
        }
    }.flowOn(Dispatchers.IO)


}
