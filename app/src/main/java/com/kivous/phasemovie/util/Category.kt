package com.kivous.phasemovie.util

enum class Category(
    val category: String = "",
    val title: String = "",
    val region: String = "",
    val shuffle: Boolean = false,
    val mediaType: String = "movie",
    val sortBy: String = "popularity.desc",
    val originCountry: String = "",
    var language: String = "",
    val genres: String = "",
    val keywords: String = "",
    val primaryReleaseDateGte: String = "",
    val isCardWide: Boolean = false,
    val showLanguageFilter: Boolean = false,
) {
    SLIDER(
        category = "slider",
        title = "Slider"
    ),
    LATEST_RELEASES_IN_INDIA(
        category = "now_playing",
        title = "Latest Releases in India",
        region = "IN",
        shuffle = true
    ),
    SIMILAR(
        category = "similar",
        title = "More Like This"
    ),

    TRENDING_MOVIES(
        category = "trending_movies",
        title = "Trending Movies Today",
        shuffle = true
    ),

    POPULAR_INDIAN_TV_SHOWS(
        category = "popular_indian_tv_shows",
        title = "Popular Indian TV Shows",
        mediaType = "tv",
        originCountry = "IN",
        isCardWide = true
    ),
    NOW_PLAYING(
        category = "now_playing",
        title = "Now Playing in Theaters",
        shuffle = true
    ),
    TOP_INDIAN_MOVIES(
        category = "top_indian_movies",
        title = "Top 20 Indian Movies",
        showLanguageFilter = true,
        language = "hi",
        originCountry = "IN"
    ),
    TOP_RATED(
        category = "top_rated",
        title = "Top Rated Worldwide"
    ),
    POPULAR(
        category = "popular",
        title = "Popular Movies",
        shuffle = true
    ),
    POPULAR_TV_SHOWS(
        category = "popular_tv_shows",
        title = "Popular TV Shows",
        shuffle = true,
        mediaType = "tv",
        isCardWide = true
    ),
    POPULAR_ANIME(
        category = "popular_anime",
        title = "Popular Anime",
        keywords = "210024"
    ),
    POPULAR_ANIMATION_MOVIES(
        category = "popular_animation_movies",
        title = "Top Animated Movies",
        shuffle = true,
        genres = "16"
    ),
    POPULAR_ANIMATION_TV_SHOWS(
        category = "popular_animation_tv_shows",
        title = "Popular Animated TV Shows",
        mediaType = "tv",
        genres = "16",
        isCardWide = true
    ),

    THRILLER_HINDI_MOVIES(
        category = "thriller_hindi_movies",
        title = "Must-Watch Hindi Thrillers",
        originCountry = "IN",
        language = "hi",
        genres = "53"
    ),

    UPCOMING_INDIAN_MOVIES(
        category = "upcoming_indian_movies",
        title = "Upcoming Indian Movies",
        originCountry = "IN",
        primaryReleaseDateGte = Common.tomorrowDate()
    ),
    UPCOMING_MOVIES(
        category = "upcoming_movies",
        title = "Coming Soon",
        primaryReleaseDateGte = Common.tomorrowDate()
    ),

}