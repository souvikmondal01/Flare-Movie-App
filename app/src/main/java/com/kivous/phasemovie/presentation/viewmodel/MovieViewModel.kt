package com.kivous.phasemovie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.repository.MovieRepository
import com.kivous.phasemovie.presentation.state.LoadState
import com.kivous.phasemovie.presentation.state.MovieState
import com.kivous.phasemovie.presentation.state.MoviesWrapper
import com.kivous.phasemovie.util.Category
import com.kivous.phasemovie.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun paginate(category: Category, id: String = "") {
        val wrapper = movieState.value.moviesState[category] ?: return
        if (wrapper.loadState == LoadState.Loading) return
        when (category) {
            Category.LATEST_RELEASES_IN_INDIA,
            Category.NOW_PLAYING,
            Category.POPULAR,
            Category.TOP_RATED -> getMovies(category)

            Category.TRENDING_MOVIES -> getTrendingMovies()
            Category.SIMILAR -> getSimilarMovies(id)
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
                        loadState = LoadState.Failure,
                        error = result.message.orEmpty()
                    )
                }

                state.copy(
                    moviesState = state.moviesState.toMutableMap().apply {
                        put(category, updatedWrapper)
                    }
                )
            }
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

    fun getSimilarMovies(id: String, category: Category = Category.SIMILAR) {
        fetchMovies(
            category = category,
            usePage = true,
            append = true,
        ) { page ->
            movieRepository.getSimilarMovies(id, page)
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

    fun getSliderMovies(category: Category = Category.SLIDER) {
        fetchMovies(
            category = category,
            usePage = false,
            append = false,
        ) {
            movieRepository.getSliderMovies()
        }
    }

    fun getMovie(id: String) = viewModelScope.launch {
        movieRepository.getMovie(id).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(movieLoadState = LoadState.Loading)
                    }

                }

                is Response.Success -> {
                    _movieState.update {
                        it.copy(
                            movieLoadState = LoadState.Success, movie = result.data
                        )
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(
                            movieLoadState = LoadState.Failure, movieError = result.message ?: ""
                        )
                    }
                }
            }
        }
    }

    fun getMovieCredits(id: String) = viewModelScope.launch {
        movieRepository.getMovieCredits(id).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingMovieCredits = true)
                    }

                }

                is Response.Success -> {
                    _movieState.update {
                        it.copy(
                            isLoadingMovieCredits = false, movieCredits = result.data
                        )
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(
                            isLoadingMovieCredits = false, movieCreditsError = result.message ?: ""
                        )
                    }
                }


            }
        }
    }

    fun getSocial(id: String) = viewModelScope.launch {
        movieRepository.getSocial(id).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingSocial = true)
                    }
                }

                is Response.Success -> {
                    _movieState.update {
                        it.copy(
                            isLoadingSocial = false, social = result.data
                        )
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(
                            isLoadingSocial = false, socialError = result.message ?: ""
                        )
                    }
                }
            }
        }
    }

}
