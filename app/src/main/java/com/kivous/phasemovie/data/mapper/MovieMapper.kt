package com.kivous.phasemovie.data.mapper

import com.kivous.phasemovie.data.remote.MovieApi.Companion.IMAGE_BASE_URL
import com.kivous.phasemovie.data.remote.model.MovieDto
import com.kivous.phasemovie.data.remote.model.movie_credits.CastDto
import com.kivous.phasemovie.data.remote.model.movie_credits.CrewDto
import com.kivous.phasemovie.data.remote.model.movie_credits.MovieCreditsDto
import com.kivous.phasemovie.data.remote.model.movie_details.GenreDto
import com.kivous.phasemovie.data.remote.model.movie_details.MovieDetailsDto
import com.kivous.phasemovie.domain.model.Genre
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.MovieDetails
import com.kivous.phasemovie.domain.model.movie_credits.Cast
import com.kivous.phasemovie.domain.model.movie_credits.Crew
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits

fun MovieDto.toMovie(
    category: String
): Movie = Movie(
    adult = adult == true,
    backdrop_path = IMAGE_BASE_URL + backdrop_path,
    original_language = original_language ?: "",
    overview = overview ?: "",
    poster_path = IMAGE_BASE_URL + poster_path,
    release_date = release_date ?: "0000-00-00",
    title = title ?: "",
    vote_average = vote_average ?: 0.0,
    popularity = popularity ?: 0.0,
    vote_count = vote_count ?: 0,
    id = id ?: -1,
    original_title = original_title ?: "",
    video = video == true,
    category = category,
    genre_ids = genre_ids ?: listOf(-1, -2)
)

fun MovieDetailsDto.toMovieDetails(
): MovieDetails = MovieDetails(
    adult = adult == true,
    backdropPath = IMAGE_BASE_URL + backdrop_path,
    budget = budget ?: 0,
    genres = genres?.map { it.toGenre() } ?: listOf(),
    homepage = homepage ?: "",
    id = id ?: -1,
    imdbId = imdb_id ?: "",
    originalLanguage = original_language ?: "",
    originalTitle = original_title ?: "",
    overview = overview ?: "",
    posterPath = IMAGE_BASE_URL + poster_path,
    releaseDate = release_date ?: "",
    revenue = revenue ?: 0,
    runtime = runtime ?: 0,
    status = status ?: "",
    tagline = tagline ?: "",
    title = title ?: "",
    video = video == true,
    voteAverage = vote_average ?: 0.0,
    voteCount = vote_count ?: 0,
)

fun GenreDto.toGenre(): Genre = Genre(
    id = id ?: -1, name = name ?: ""
)

fun MovieCreditsDto.toMovieCredits(): MovieCredits = MovieCredits(
    cast = cast?.map { it.toCast() } ?: emptyList(),
    crew = crew?.map { it.toCrew() } ?: emptyList(),
    id = id ?: -1
)

fun CastDto.toCast(): Cast = Cast(
    castId = cast_id ?: -1,
    character = character ?: "",
    creditId = credit_id ?: "",
    gender = gender ?: -1,
    id = id ?: -1,
    knownForDepartment = known_for_department ?: "",
    name = name ?: "",
    order = order ?: 9999,
    profilePath = IMAGE_BASE_URL + profile_path
)

fun CrewDto.toCrew(): Crew = Crew(
    creditId = credit_id ?: "",
    department = department ?: "",
    gender = gender ?: -1,
    id = id ?: -1,
    job = job ?: "",
    knownForDepartment = known_for_department ?: "",
    name = name ?: "",
    profilePath = IMAGE_BASE_URL + profile_path
)