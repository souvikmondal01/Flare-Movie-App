package com.souvikmondal01.flare.presentation.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.domain.model.Movie
import com.souvikmondal01.flare.domain.model.Social
import com.souvikmondal01.flare.domain.model.credits.Credits
import com.souvikmondal01.flare.domain.repository.MovieRepository
import com.souvikmondal01.flare.presentation.state.LoadState
import com.souvikmondal01.flare.presentation.state.MovieState
import com.souvikmondal01.flare.presentation.state.MoviesWrapper
import com.souvikmondal01.flare.util.Category
import com.souvikmondal01.flare.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private var _movieState = MutableStateFlow(MovieState())
    val movieState = _movieState.asStateFlow()

    fun paginate(category: Category, id: Int = -1) {
        val wrapper = movieState.value.moviesState[category] ?: return
        if (wrapper.loadState == LoadState.Loading) return
        loadMoviesByCategory(category = category, id = id)
    }

    fun loadMoviesByCategory(
        category: Category,
        id: Int = -1,
        languageCode: String = "",
        lazyListState: LazyListState? = null,
        scrollToFirst: Boolean = true
    ) {
        when (category) {
            Category.LATEST_RELEASES_IN_INDIA, Category.NOW_PLAYING, Category.POPULAR, Category.TOP_RATED -> getMovies(
                category
            )

            Category.TRENDING_MOVIES -> getTrendingMovies()
            Category.SIMILAR_MOVIE -> getSimilarMedia(id, category)
            Category.SIMILAR_TV -> getSimilarMedia(id, category)
            Category.TOP_INDIAN_MOVIES -> {
                getDiscoverMedia(
                    category = category, language = languageCode, usePage = false, append = false
                )
                if (scrollToFirst) {
                    lazyListState?.let {
                        viewModelScope.launch {
                            delay(50)
                            it.scrollToItem(0)
                        }
                    }
                }
            }

            else -> getDiscoverMedia(category)
        }
    }

    fun fetchMovies(
        category: Category,
        usePage: Boolean = true,
        append: Boolean = true,
        fetch: suspend (page: Int) -> Flow<Response<List<Movie>>>
    ) = viewModelScope.launch {
        val current = movieState.value.moviesState[category] ?: MoviesWrapper()
        val page = if (usePage) current.page else 1

        fetch(page).collectLatest { result ->
            _movieState.update { state ->
                val movies =
                    result.data.orEmpty().let { if (category.shuffle) it.shuffled() else it }

                val updatedWrapper = when (result) {
                    is Response.Loading -> current.copy(loadState = LoadState.Loading)

                    is Response.Success -> current.copy(
                        loadState = LoadState.Success,
                        movies = if (append) current.movies + movies else movies,
                        page = if (usePage) current.page + 1 else current.page
                    )

                    is Response.Error -> current.copy(
                        loadState = LoadState.Failure, error = result.message.orEmpty()
                    )
                }

                state.copy(
                    moviesState = state.moviesState.toMutableMap().apply {
                        put(category, updatedWrapper)
                    })
            }
        }
    }

    fun getSliderMovies(category: Category = Category.SLIDER) {
        fetchMovies(
            category = category,
            usePage = false,
            append = false,
        ) {
            movieRepository.getSliderMovies()
        }
    }

    fun getMovies(category: Category) {
        fetchMovies(
            category = category,
            usePage = true,
            append = true,
        ) { page ->
            movieRepository.getMovies(category, page)
        }
    }

    fun getTrendingMovies(category: Category = Category.TRENDING_MOVIES) = viewModelScope.launch {
        fetchMovies(
            category = category,
            usePage = true,
            append = true,
        ) { page ->
            movieRepository.getTrendingMovies(page = page)
        }
    }

    fun getDiscoverMedia(
        category: Category,
        language: String = category.language,
        usePage: Boolean = true,
        append: Boolean = true,
    ) = viewModelScope.launch {
        fetchMovies(
            category = category,
            usePage = usePage,
            append = append,
        ) { page ->
            movieRepository.discoverMedia(
                mediaType = category.mediaType,
                page = page,
                sortBy = category.sortBy,
                withOriginCountry = category.originCountry,
                withOriginalLanguage = language,
                withGenres = category.genres,
                withKeywords = category.keywords,
                primaryReleaseDateGte = category.primaryReleaseDateGte
            )
        }
    }

    fun getSimilarMedia(id: Int, category: Category) {
        fetchMovies(
            category = category,
            usePage = true,
            append = true,
        ) { page ->
            movieRepository.getSimilarMedia(
                mediaType = category.mediaType, id = id, page = page
            )
        }
    }

    private fun updateMediaStateFromResponse(response: Response<Movie>) {
        _movieState.update {
            when (response) {
                is Response.Loading -> it.copy(
                    movieLoadState = LoadState.Loading, movieError = ""
                )

                is Response.Success -> it.copy(
                    movieLoadState = LoadState.Success, movie = response.data, movieError = ""
                )

                is Response.Error -> it.copy(
                    movieLoadState = LoadState.Failure,
                    movieError = response.message ?: "Something went wrong"
                )
            }
        }
    }

    fun getMedia(mediaType: MediaType, id: Int) = viewModelScope.launch {
        movieRepository.getMedia(mediaType, id).collectLatest { response ->
            updateMediaStateFromResponse(response)
        }
    }

    private fun updateCertificationStateFromResponse(response: Response<String>) {
        _movieState.update {
            when (response) {
                is Response.Loading -> it.copy(
                    certificationLoadState = LoadState.Loading,
                    certificationError = "",
                )

                is Response.Success -> it.copy(
                    certificationLoadState = LoadState.Success,
                    certification = response.data ?: "N/A",
                    certificationError = "",
                )

                is Response.Error -> it.copy(
                    certificationLoadState = LoadState.Failure,
                    certificationError = response.message ?: "Something went wrong"
                )
            }
        }
    }

    fun getCertification(mediaType: MediaType, id: Int) = viewModelScope.launch {
        movieRepository.getCertification(mediaType = mediaType, id = id).collectLatest { response ->
            updateCertificationStateFromResponse(response)
        }
    }

    private fun updateCreditsStateFromResponse(response: Response<Credits>) {
        _movieState.update {
            when (response) {
                is Response.Loading -> it.copy(
                    creditsLoadState = LoadState.Loading, creditsError = ""
                )

                is Response.Success -> it.copy(
                    creditsLoadState = LoadState.Success, credits = response.data, creditsError = ""
                )

                is Response.Error -> it.copy(
                    creditsLoadState = LoadState.Failure,
                    creditsError = response.message ?: "Something went wrong"
                )
            }
        }
    }

    fun getCredits(mediaType: MediaType, id: Int) = viewModelScope.launch {
        movieRepository.getCredits(mediaType = mediaType, id = id).collectLatest { result ->
            updateCreditsStateFromResponse(result)
        }
    }

    private fun updateSocialsStateFromResponse(response: Response<Social>) {
        _movieState.update {
            when (response) {
                is Response.Loading -> it.copy(
                    socialLoadState = LoadState.Loading, socialError = ""
                )

                is Response.Success -> it.copy(
                    socialLoadState = LoadState.Success,
                    social = response.data ?: Social(-1, "", "", "", "", ""),
                    socialError = ""
                )

                is Response.Error -> it.copy(
                    socialLoadState = LoadState.Failure,
                    socialError = response.message ?: "Something went wrong"
                )
            }
        }
    }

    fun getSocials(mediaType: MediaType, id: Int) = viewModelScope.launch {
        movieRepository.getSocials(mediaType = mediaType, id = id).collectLatest { response ->
            updateSocialsStateFromResponse(response)
        }
    }

    private val _searchMovieState = MutableStateFlow(MoviesWrapper())
    val searchMovieState = _searchMovieState.asStateFlow()

    fun searchPaginate(query: String) {
        if (searchMovieState.value.loadState == LoadState.Loading) return
        getSearchMovies(query)
    }

    fun clearSearch() {
        _searchMovieState.update { MoviesWrapper() }
    }

    fun getSearchMovies(query: String) = viewModelScope.launch {
        val page = searchMovieState.value.page
        val movies = searchMovieState.value.movies

        movieRepository.search(query = query, page = page).collectLatest { response ->
            _searchMovieState.update {
                when (response) {
                    is Response.Loading -> it.copy(
                        loadState = LoadState.Loading, error = ""
                    )

                    is Response.Success -> it.copy(
                        loadState = LoadState.Success,
                        movies = movies + response.data.orEmpty(),
                        page = page + 1,
                        error = ""
                    )

                    is Response.Error -> it.copy(
                        loadState = LoadState.Failure,
                        error = response.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun insertMedia(movie: Movie) = viewModelScope.launch {
        movieRepository.insertMedia(movie).collectLatest {}
    }

    fun deleteMediaById(id: Int) = viewModelScope.launch {
        movieRepository.deleteMediaById(id).collectLatest {
        }
    }

    fun getAllMedia() = viewModelScope.launch {
        movieRepository.getAllMedia().collectLatest {
            _movieState.update { state ->
                when (it) {
                    is Response.Loading -> state.copy(
                        localDBLoadState = LoadState.Loading,
                        localDBError = ""
                    )

                    is Response.Success -> state.copy(
                        localDBLoadState = LoadState.Success,
                        localDBMovies = it.data.orEmpty(),
                        localDBError = ""
                    )

                    is Response.Error -> state.copy(
                        localDBLoadState = LoadState.Failure,
                        localDBError = it.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun isMediaSaved(id: Int) = viewModelScope.launch {
        movieRepository.isMediaSaved(id).collectLatest {
            _movieState.update { state ->
                state.copy(isMediaSaved = it.data == true)
            }
        }
    }

}