package com.kivous.phasemovie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kivous.phasemovie.domain.repository.MovieListRepository
import com.kivous.phasemovie.presentation.state.MovieListState
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
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getSliderMovieList()
        getNowPlayingMovieList()
        getPopularMovieList()
        getTopRatedMovieList()
        getUpcomingMovieList()
    }

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

    private fun getNowPlayingMovieList() = viewModelScope.launch {
        movieListRepository.getMovieList(
            Category.NOW_PLAYING.category,
            movieListState.value.nowPlayingMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieListState.update {
                        it.copy(isLoadingNowPlaying = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { nowPlayingMovieList ->
                        _movieListState.update {
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
                    _movieListState.update {
                        it.copy(isLoadingNowPlaying = false)
                    }
                }
            }
        }
    }

    private fun getPopularMovieList() = viewModelScope.launch {
        movieListRepository.getMovieList(
            Category.POPULAR.category,
            movieListState.value.popularMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieListState.update {
                        it.copy(isLoadingPopular = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { popularMovieList ->
                        _movieListState.update {
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
                    _movieListState.update {
                        it.copy(isLoadingPopular = false)
                    }
                }
            }
        }
    }

    private fun getTopRatedMovieList() = viewModelScope.launch {
        movieListRepository.getMovieList(
            Category.TOP_RATED.category,
            movieListState.value.topRatedMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieListState.update {
                        it.copy(isLoadingTopRated = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { topRatedMovieList ->
                        _movieListState.update {
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
                    _movieListState.update {
                        it.copy(isLoadingTopRated = false)
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList() = viewModelScope.launch {
        movieListRepository.getMovieList(
            Category.UPCOMING.category,
            movieListState.value.upcomingMovieListPage
        ).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieListState.update {
                        it.copy(isLoadingUpcoming = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { upcomingMovieList ->
                        _movieListState.update {
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
                    _movieListState.update {
                        it.copy(isLoadingUpcoming = false)
                    }
                }
            }
        }
    }

    fun getSimilarMovieList(id: String) = viewModelScope.launch {
        movieListRepository.getSimilarMovies(id).collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieListState.update {
                        it.copy(isLoadingSimilar = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { list ->
                        _movieListState.update {
                            it.copy(
                                isLoadingSimilar = false,
                                similarMovieList = list.shuffled()
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieListState.update {
                        it.copy(isLoadingSimilar = false)
                    }
                }
            }
        }
    }

    private fun getSliderMovieList() = viewModelScope.launch {
        movieListRepository.getSliderMovieList().collectLatest { result ->
            when (result) {
                is Response.Loading -> {
                    _movieListState.update {
                        it.copy(isLoadingSliderMovie = true)
                    }
                }

                is Response.Success -> {
                    result.data?.let { data ->
                        _movieListState.update {
                            it.copy(
                                isLoadingSliderMovie = false,
                                sliderMovieList = data
                            )
                        }
                    }
                }

                is Response.Error -> {
                    _movieListState.update {
                        it.copy(
                            isLoadingSliderMovie = false,
                            sliderMovieError = result.message ?: ""
                        )
                    }
                }

            }
        }
    }

}
