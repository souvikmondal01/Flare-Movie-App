package com.souvikmondal01.flare.data.mapper

import com.souvikmondal01.flare.data.remote.MovieApi.Companion.IMAGE_BASE_URL
import com.souvikmondal01.flare.data.remote.model.MovieDto
import com.souvikmondal01.flare.data.remote.model.SliderMovie
import com.souvikmondal01.flare.data.remote.model.SocialDto
import com.souvikmondal01.flare.data.remote.model.credits.CastDto
import com.souvikmondal01.flare.data.remote.model.credits.CreditsDto
import com.souvikmondal01.flare.data.remote.model.credits.CrewDto
import com.souvikmondal01.flare.data.remote.model.movie_details.MovieDetailsDto
import com.souvikmondal01.flare.data.remote.model.tv_details.TvDetailsDto
import com.souvikmondal01.flare.data.local.model.Media
import com.souvikmondal01.flare.domain.model.Movie
import com.souvikmondal01.flare.domain.model.Social
import com.souvikmondal01.flare.domain.model.credits.Cast
import com.souvikmondal01.flare.domain.model.credits.Credits
import com.souvikmondal01.flare.domain.model.credits.Crew

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
    certification = "",
    topCast = emptyList(),
    social = social,
    mediaType = "movie"
)

fun MovieDto.toMovie() = Movie(
    adult = adult == true,
    backdropPath = backdrop_path?.let { IMAGE_BASE_URL + it }.orEmpty(),
    budget = 0,
    genres = genre_ids?.map { it.toString() } ?: emptyList(),
    homepage = "",
    id = id ?: -1,
    imdbId = "",
    originalLanguage = original_language ?: "xx",
    originalTitle = original_title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = poster_path?.let { IMAGE_BASE_URL + it }.orEmpty(),
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
    certification = "",
    topCast = emptyList(),
    social = social,
    mediaType = media_type.orEmpty()
)

fun MovieDetailsDto.toMovie(): Movie = Movie(
    adult = adult == true,
    backdropPath = backdrop_path?.let { IMAGE_BASE_URL + it }.orEmpty(),
    budget = budget ?: 0,
    genres = genres?.map { it.name.orEmpty() }.orEmpty(),
    homepage = homepage.orEmpty(),
    id = id ?: -1,
    imdbId = imdb_id.orEmpty(),
    originalLanguage = original_language.orEmpty(),
    originalTitle = original_title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = poster_path?.let { IMAGE_BASE_URL + it }.orEmpty(),
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
    certification = "",
    topCast = emptyList(),
    social = social,
    mediaType = "movie"
)

fun TvDetailsDto.toMovie(): Movie = Movie(
    adult = adult == true,
    backdropPath = backdrop_path?.let { IMAGE_BASE_URL + it }.orEmpty(),
    budget = 0,
    genres = genres?.map { it.name.orEmpty() }.orEmpty(),
    homepage = homepage.orEmpty(),
    id = id ?: -1,
    imdbId = "",
    originalLanguage = original_language.orEmpty(),
    originalTitle = original_name.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = poster_path?.let { IMAGE_BASE_URL + it }.orEmpty(),
    releaseDate = first_air_date.orEmpty(),
    revenue = 0,
    runtime = 0,
    status = status.orEmpty(),
    tagline = tagline.orEmpty(),
    title = name.orEmpty(),
    video = false,
    voteAverage = vote_average ?: 0.0,
    voteCount = vote_count ?: 0,
    firstAirDate = first_air_date.orEmpty(),
    name = name.orEmpty(),
    certification = "",
    topCast = emptyList(),
    social = social,
    mediaType = "tv"
)

fun CreditsDto.toMovieCredits(): Credits = Credits(
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
    profilePath = profile_path?.let { IMAGE_BASE_URL + it }.orEmpty()
)

fun CrewDto.toCrew(): Crew = Crew(
    creditId = credit_id.orEmpty(),
    department = department.orEmpty(),
    gender = gender ?: -1,
    id = id ?: -1,
    job = job.orEmpty(),
    knownForDepartment = known_for_department.orEmpty(),
    name = name.orEmpty(),
    profilePath = profile_path?.let { IMAGE_BASE_URL + it }.orEmpty()
)

fun SocialDto.toSocial(): Social = Social(
    facebookId = facebook_id.orEmpty(),
    id = id ?: -1,
    imdbId = imdb_id.orEmpty(),
    instagramId = instagram_id.orEmpty(),
    twitterId = twitter_id.orEmpty(),
    wikidataId = wikidata_id.orEmpty()
)

val social = Social(
    facebookId = "", id = -1, imdbId = "", instagramId = "", twitterId = "", wikidataId = ""
)

fun Movie.toMedia() = Media(
    id = id,
    title = title,
    posterPath = posterPath,
    runtime = runtime,
    voteAverage = voteAverage,
    certification = certification,
    releaseDate = releaseDate,
    originalLanguage = originalLanguage,
    mediaType = mediaType,
    createdAt = System.currentTimeMillis()
)

fun Media.toMovie() = Movie(
    adult = false,
    backdropPath = "",
    budget = 0,
    genres = emptyList(),
    homepage = "",
    id = id,
    imdbId = "",
    originalLanguage = originalLanguage,
    originalTitle = "",
    overview = "",
    posterPath = posterPath,
    releaseDate = releaseDate,
    revenue = 0,
    runtime = runtime,
    status = "",
    tagline = "",
    title = title,
    video = false,
    voteAverage = voteAverage,
    voteCount = 0,
    firstAirDate = releaseDate,
    name = "",
    certification = certification,
    topCast = emptyList(),
    social = social,
    mediaType = mediaType
)