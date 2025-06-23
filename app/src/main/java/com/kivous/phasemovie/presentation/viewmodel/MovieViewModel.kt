package com.kivous.phasemovie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kivous.phasemovie.domain.repository.MovieRepository
import com.kivous.phasemovie.presentation.state.MovieState
import com.kivous.phasemovie.util.Category
import com.kivous.phasemovie.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val movieListState = _movieState.asStateFlow()

    fun paginate(category: Category) {
        when (category) {
            Category.NOW_PLAYING -> {
                getNowPlayingMovieList()
            }

            Category.POPULAR -> {
                getPopularMovieList()
            }

            Category.TOP_RATED -> {
                getTopRatedMovieList()
            }

            Category.UPCOMING -> {
                getUpcomingMovieList()
            }

            else -> {}
        }
    }

    fun getNowPlayingMovieList() = viewModelScope.launch {
        movieRepository.getMovieList(
            Category.NOW_PLAYING.category,
            movieListState.value.nowPlayingMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingNowPlaying = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { nowPlayingMovieList ->
                        _movieState.update {
                            it.copy(
                                isLoadingNowPlaying = false,
                                nowPlayingMovieList =
                                    movieListState.value.nowPlayingMovieList
                                            + nowPlayingMovieList.shuffled(),
                                nowPlayingMovieListPage = movieListState.value.nowPlayingMovieListPage + 1
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(isLoadingNowPlaying = false)
                    }
                }
            }
        }
    }

    fun getPopularMovieList() = viewModelScope.launch {
        movieRepository.getMovieList(
            Category.POPULAR.category,
            movieListState.value.popularMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingPopular = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { popularMovieList ->
                        _movieState.update {
                            it.copy(
                                isLoadingPopular = false,
                                popularMovieList =
                                    movieListState.value.popularMovieList
                                            +
                                            popularMovieList
                                                .shuffled(),
                                popularMovieListPage = movieListState.value.popularMovieListPage + 1
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(isLoadingPopular = false)
                    }
                }
            }
        }
    }

    fun getTopRatedMovieList() = viewModelScope.launch {
        movieRepository.getMovieList(
            Category.TOP_RATED.category,
            movieListState.value.topRatedMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingTopRated = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { topRatedMovieList ->
                        _movieState.update {
                            it.copy(
                                isLoadingTopRated = false,
                                topRatedMovieList =
                                    movieListState.value.topRatedMovieList
                                            + topRatedMovieList.shuffled(),
                                topRatedMovieListPage = movieListState.value.topRatedMovieListPage + 1
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(isLoadingTopRated = false)
                    }
                }
            }
        }
    }

    fun getUpcomingMovieList() = viewModelScope.launch {
        movieRepository.getMovieList(
            Category.UPCOMING.category,
            movieListState.value.upcomingMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingUpcoming = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { upcomingMovieList ->
                        _movieState.update {
                            it.copy(
                                isLoadingUpcoming = false,
                                upcomingMovieList =
                                    movieListState.value.upcomingMovieList
                                            + upcomingMovieList.shuffled(),
                                upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(isLoadingUpcoming = false)
                    }
                }
            }
        }
    }

    fun getSimilarMovieList(id: String) = viewModelScope.launch {
        movieRepository.getSimilarMovies(id).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingSimilar = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { list ->
                        _movieState.update {
                            it.copy(
                                isLoadingSimilar = false,
                                similarMovieList = list
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(isLoadingSimilar = false)
                    }
                }
            }
        }
    }

    fun getSliderMovieList() = viewModelScope.launch {
        movieRepository.getSliderMovieList().collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingSliderMovie = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { data ->
                        _movieState.update {
                            it.copy(
                                isLoadingSliderMovie = false,
                                sliderMovieList = data
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(
                            isLoadingSliderMovie = false,
                            sliderMovieError = result.message ?: ""
                        )
                    }
                }

            }
        }
    }

    fun getMovieDetails(id: String) = viewModelScope.launch {
        movieRepository.getMovieDetails(id).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieState.update {
                        it.copy(isLoadingMovieDetails = true)
                    }

                }

                is Response.Success -> {
                    _movieState.update {
                        it.copy(
                            isLoadingMovieDetails = false,
                            movieDetails = result.data
                        )
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(
                            isLoadingMovieDetails = false,
                            movieDetailsError = result.message ?: ""
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
                            isLoadingMovieCredits = false,
                            movieCredits = result.data
                        )
                    }
                }

                is Response.Error -> {
                    _movieState.update {
                        it.copy(
                            isLoadingMovieCredits = false,
                            movieCreditsError = result.message ?: ""
                        )
                    }
                }


            }
        }
    }


}
