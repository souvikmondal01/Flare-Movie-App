package com.kivous.phasemovie.data.mapper

import com.kivous.phasemovie.data.remote.MovieApi.Companion.IMAGE_BASE_URL
import com.kivous.phasemovie.data.remote.model.MovieDto
import com.kivous.phasemovie.data.remote.model.SliderMovie
import com.kivous.phasemovie.data.remote.model.SocialDto
import com.kivous.phasemovie.data.remote.model.movie_credits.CastDto
import com.kivous.phasemovie.data.remote.model.movie_credits.CrewDto
import com.kivous.phasemovie.data.remote.model.movie_credits.MovieCreditsDto
import com.kivous.phasemovie.data.remote.model.movie_details.MovieDetailsDto
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.domain.model.Social
import com.kivous.phasemovie.domain.model.movie_credits.Cast
import com.kivous.phasemovie.domain.model.movie_credits.Crew
import com.kivous.phasemovie.domain.model.movie_credits.MovieCredits

fun MovieDto.toMovie() = Movie(
    adult = adult == true,
    backdropPath = backdrop_path?.let { IMAGE_BASE_URL + it } ?: "",
    budget = 0,
    genres = genre_ids?.map { it.toString() } ?: emptyList(),
    homepage = "",
    id = id ?: -1,
    imdbId = "",
    originalLanguage = original_language ?: "xx",
    originalTitle = original_title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = poster_path?.let { IMAGE_BASE_URL + it } ?: "",
    releaseDate = release_date.orEmpty(),
    revenue = 0,
    runtime = 0,
    status = "",
    tagline = "",
    title = title.orEmpty(),
    video = video == true,
    voteAverage = vote_average ?: 0.0,
    voteCount = vote_count ?: 0,
    firstAirDate = first_air_date.orEmpty(),
    name = name.orEmpty(),
)

fun SliderMovie.toMovie() = Movie(
    adult = false,
    backdropPath = bgUrl.orEmpty(),
    budget = 0,
    genres = genres.orEmpty(),
    homepage = "",
    id = id?.toInt() ?: 0,
    imdbId = "",
    originalLanguage = "",
    originalTitle = name.orEmpty(),
    overview = "",
    posterPath = fgUrl.orEmpty(),
    releaseDate = "",
    revenue = 0,
    runtime = 0,
    status = "",
    tagline = "",
    title = name.orEmpty(),
    video = false,
    voteAverage = 0.0,
    voteCount = 0,
    firstAirDate = "",
    name = name.orEmpty(),
)

fun MovieDetailsDto.toMovie(
): Movie = Movie(
    adult = adult == true,
    backdropPath = backdrop_path?.let { IMAGE_BASE_URL + it } ?: "",
    budget = budget ?: 0,
    genres = genres?.map { it.name.orEmpty() }.orEmpty(),
    homepage = homepage.orEmpty(),
    id = id ?: -1,
    imdbId = imdb_id.orEmpty(),
    originalLanguage = original_language.orEmpty(),
    originalTitle = original_title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = poster_path?.let { IMAGE_BASE_URL + it } ?: "",
    releaseDate = release_date.orEmpty(),
    revenue = revenue ?: 0,
    runtime = runtime ?: 0,
    status = status.orEmpty(),
    tagline = tagline.orEmpty(),
    title = title.orEmpty(),
    video = video == true,
    voteAverage = vote_average ?: 0.0,
    voteCount = vote_count ?: 0,
    firstAirDate = "",
    name = "",
)

fun MovieCreditsDto.toMovieCredits(): MovieCredits =
    MovieCredits(
        cast = cast?.map { it.toCast() }.orEmpty(),
        crew = crew?.map { it.toCrew() }.orEmpty(),
        id = id ?: -1
    )

fun CastDto.toCast(): Cast = Cast(
    castId = cast_id ?: -1,
    character = character.orEmpty(),
    creditId = credit_id.orEmpty(),
    gender = gender ?: -1,
    id = id ?: -1,
    knownForDepartment = known_for_department.orEmpty(),
    name = name.orEmpty(),
    order = order ?: 9999,
    profilePath = IMAGE_BASE_URL + profile_path
)

fun CrewDto.toCrew(): Crew = Crew(
    creditId = credit_id.orEmpty(),
    department = department.orEmpty(),
    gender = gender ?: -1,
    id = id ?: -1,
    job = job.orEmpty(),
    knownForDepartment = known_for_department.orEmpty(),
    name = name.orEmpty(),
    profilePath = profile_path?.let { IMAGE_BASE_URL + it } ?: ""
)

fun SocialDto.toSocial(): Social = Social(
    facebookId = facebook_id.orEmpty(),
    id = id ?: -1,
    imdbId = imdb_id.orEmpty(),
    instagramId = instagram_id.orEmpty(),
    twitterId = twitter_id.orEmpty(),
    wikidataId = wikidata_id.orEmpty()
)