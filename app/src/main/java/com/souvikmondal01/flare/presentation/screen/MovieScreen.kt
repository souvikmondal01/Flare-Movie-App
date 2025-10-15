package com.souvikmondal01.flare.presentation.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.souvikmondal01.flare.R
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.domain.model.Movie
import com.souvikmondal01.flare.domain.model.Social
import com.souvikmondal01.flare.domain.model.credits.Cast
import com.souvikmondal01.flare.presentation.compoment.ImageCard
import com.souvikmondal01.flare.presentation.compoment.MovieCategoryRow
import com.souvikmondal01.flare.presentation.compoment.Spacer
import com.souvikmondal01.flare.presentation.compoment.StatusBarColor
import com.souvikmondal01.flare.presentation.compoment.getAverageColor
import com.souvikmondal01.flare.presentation.compoment.shadowEffect
import com.souvikmondal01.flare.presentation.state.LoadState
import com.souvikmondal01.flare.presentation.viewmodel.MovieViewModel
import com.souvikmondal01.flare.util.Category
import com.souvikmondal01.flare.util.Common.formatDate
import com.souvikmondal01.flare.util.Common.formatMinutesToDuration
import com.souvikmondal01.flare.util.Common.formatNumberShort
import com.souvikmondal01.flare.util.Common.getLanguageName

@Composable
fun MovieScreen(
    id: Int = 0,
    mediaType: MediaType,
    onBackClick: () -> Unit = {},
    onSimilarMovieClick: (Int, MediaType) -> Unit = { _, _ -> },
    onFullCreditsClick: (Int, MediaType) -> Unit = { _, _ -> },
    movieViewModel: MovieViewModel = hiltViewModel(),
) {
    LaunchedEffect(id) {
        movieViewModel.apply {
            getMedia(mediaType = mediaType, id = id)
            getCertification(mediaType = mediaType, id = id)
            getCredits(mediaType = mediaType, id = id)
            getSocials(mediaType = mediaType, id = id)
            isMediaSaved(id = id)
        }
    }

    val movieState by movieViewModel.movieState.collectAsStateWithLifecycle()
    val certification = movieState.certification
    val topCast = movieState.credits?.cast.orEmpty().take(7)
    val social = movieState.social
    val movie =
        movieState.movie?.copy(certification = certification, topCast = topCast, social = social)
    val isMediaSaved = movieState.isMediaSaved

    val isLoading = (movieState.movieLoadState == LoadState.Loading) && movie?.id == null

    // Status Bar Color
    val scrollState = rememberScrollState()
    var avgColor by remember { mutableStateOf(Color.Transparent) }
    StatusBarColor(scrollState = scrollState.value, color = avgColor)
    val posterPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(movie?.posterPath)
            .size(Size.ORIGINAL).build()
    ).state
    if (posterPainter is AsyncImagePainter.State.Success) {
        avgColor = getAverageColor(
            imageBitmap = posterPainter.result.drawable.toBitmap().asImageBitmap()
        )
    }
    val placeHolderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            MovieHeader(
                backdropUrl = movie?.backdropPath.orEmpty(),
                posterUrl = movie?.posterPath.orEmpty(),
            )

            MovieTitleSection(
                isLoading = isLoading, title = movie?.title.orEmpty()
            )

            MovieInfoRow(movie = movie, isLoading = isLoading)

            MovieGenresSection(
                genres = movie?.genres.orEmpty(),
                isLoading = isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            OverviewSection(
                isLoading = isLoading, overview = movie?.overview.orEmpty()
            )

            CastSection(
                isLoading = isLoading, cast = movie?.topCast.orEmpty(), onSeeAllClick = {
                    onFullCreditsClick(id, mediaType)
                })

            ReleaseDateSection(
                releaseDate = movie?.releaseDate.orEmpty(), isLoading = isLoading
            )

            RuntimeSection(
                runtime = formatMinutesToDuration(movie?.runtime), isLoading = isLoading
            )

            val budget = if (formatNumberShort(movie?.budget).isEmpty()) "-"
            else "$${formatNumberShort(movie?.budget)}"
            val revenue = if (formatNumberShort(movie?.revenue).isEmpty()) "-"
            else "$${formatNumberShort(movie?.revenue)}"
            val status = movie?.status ?: "-"
            val borderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)

            StatusBudgetRevenueRow(
                status = status,
                budget = budget,
                revenue = revenue,
                borderColor = borderColor,
                isLoading = isLoading
            )

            SocialSection(
                social = movie?.social, homepage = movie?.homepage.orEmpty(), isLoading = isLoading
            )

            if (!isLoading) {
                val category =
                    if (mediaType == MediaType.Movie) Category.SIMILAR_MOVIE else Category.SIMILAR_TV
                MovieCategoryRow(
                    category = category,
                    viewModel = movieViewModel,
                    state = movieState,
                    onMovieClick = { onSimilarMovieClick(it, mediaType) },
                    showMore = false,
                    movieId = id,
                    showPlaceholder = false
                )
            }
            Spacer(height = 96.dp)
        }

        // Back button start
        AnimatedVisibility(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp),
            visible = scrollState.value < 200,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0f))
                    .clickable { onBackClick() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = if (isLoading) placeHolderColor else MaterialTheme.colorScheme.onBackground.copy(
                        alpha = .75f
                    ),
                )
            }
        }
        // Back button end

        // Save button start
        AnimatedVisibility(
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp),
            visible = scrollState.value < 200,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 })
        ) {
            FavoriteToggleButton(
                favourite = isMediaSaved, onToggle = {
                    if (isMediaSaved) {
                        movieViewModel.deleteMediaById(id)
                    } else {
                        movie?.let {
                            movieViewModel.insertMedia(movie)
                        }
                    }
                }, placeHolderColor = placeHolderColor, isLoading = isLoading
            )
        }
        // Save button end
    }

    BackHandler {
        onBackClick()
    }
}

@Composable
fun MovieHeader(
    backdropUrl: String,
    posterUrl: String,
    modifier: Modifier = Modifier,
    placeHolderColor: Color = MaterialTheme.colorScheme.onBackground.copy(.05f),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        // Blurred backdrop image
        ImageCard(
            imagePath = backdropUrl,
            modifier = Modifier
                .fillMaxSize()
                .blur(4.dp),
            alpha = 0.6f,
            cornerRadius = 0.dp,
            backgroundColor = placeHolderColor,
            clickable = false
        )

        // Shadow or gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadowEffect()
        )

        // Poster image at the bottom center
        ImageCard(
            imagePath = posterUrl,
            modifier = Modifier.align(Alignment.BottomCenter),
            cornerRadius = 12.dp,
            height = 164.dp,
            width = 108.dp,
            backgroundColor = placeHolderColor,
            clickable = false
        )
    }
    Spacer(height = 16.dp)

}

@Composable
fun MovieTitleSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    title: String,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            PlaceholderBox(
                width = 180.dp,
                height = 24.dp,
            )
        } else {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
    Spacer(height = 16.dp)
}

@SuppressLint("DefaultLocale")
@Composable
fun MovieInfoRow(
    movie: Movie?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground.copy(.75f),
    placeHolderColor: Color = MaterialTheme.colorScheme.onBackground.copy(.05f)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Certification
        if (isLoading) {
            PlaceholderBox(width = 32.dp, height = 20.dp)
        } else {
            movie?.let {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.certification,
                        color = textColor,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp)
                    )
                }
            }
        }
        Spacer(width = 16.dp)

        // Dot Separator
        if (isLoading) {
            Text(text = "•", color = placeHolderColor, fontSize = 20.sp)
        } else {
            movie?.let {
                Text(text = "•", color = textColor, fontSize = 20.sp)
            }
        }
        Spacer(width = 16.dp)

        // Language
        if (isLoading) {
            PlaceholderBox(width = 64.dp, height = 20.dp)
        } else {
            movie?.let {
                Text(
                    text = getLanguageName(movie.originalLanguage),
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        Spacer(width = 16.dp)

        // Dot Separator
        if (isLoading) {
            Text(text = "•", color = placeHolderColor, fontSize = 20.sp)
        } else {
            movie?.let {
                Text(text = "•", color = textColor, fontSize = 20.sp)
            }
        }
        Spacer(width = 16.dp)

        // Rating
        if (isLoading) {
            PlaceholderBox(width = 40.dp, height = 20.dp)
        } else {
            movie?.let {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.1f", movie.voteAverage / 2),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(12.dp)
                            .padding(start = 2.dp)
                    )
                }
            }
        }
    }
    Spacer(height = 16.dp)
}

@Composable
fun MovieGenresSection(
    genres: List<String>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        PlaceholderBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(20.dp)
        )
        Spacer(height = 16.dp)
    } else {
        if (genres.isNotEmpty()) {
            Text(
                text = genres.joinToString(separator = "  |  "),
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )
            Spacer(height = 16.dp)
        }
    }
}

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    overview: String,
) {
    if (isLoading) {
        PlaceholderBox(
            modifier = modifier
                .fillMaxWidth()
                .height(124.dp)
                .padding(horizontal = 8.dp),
            cornerRadius = 8.dp
        )
        Spacer(height = 16.dp)
    } else {
        if (overview.isNotEmpty()) {
            Text(
                text = overview,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(.85f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
            Spacer(height = 16.dp)
        }
    }
}

@Composable
fun CastSection(
    modifier: Modifier = Modifier, isLoading: Boolean, cast: List<Cast>, onSeeAllClick: () -> Unit
) {
    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(172.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                PlaceholderBox(
                    height = 24.dp, width = 76.dp, cornerRadius = 0.dp
                )
                PlaceholderBox(
                    height = 18.dp, width = 64.dp
                )
            }
            LazyRow(
                userScrollEnabled = false,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(5) {
                    Column(
                        modifier = Modifier.width(112.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlaceholderBox(
                            height = 64.dp, width = 64.dp, cornerRadius = 64.dp
                        )
                        PlaceholderBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .padding(horizontal = 8.dp), cornerRadius = 0.dp
                        )
                    }
                }
            }
        }
        Spacer(height = 16.dp)
    } else {
        if (cast.isNotEmpty()) {
            Column(
                modifier = modifier.height(172.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "Top Cast", style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                onSeeAllClick()
                            }
                            .padding(horizontal = 6.dp, vertical = 1.dp)) {
                        Text(
                            text = "See all",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(cast) { cast ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(112.dp)
                        ) {
                            ImageCard(
                                imagePath = cast.profilePath,
                                height = 64.dp,
                                width = 64.dp,
                                shape = CircleShape,
                                clickable = false
                            )
                            Text(
                                text = cast.name,
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                }
            }
            Spacer(height = 16.dp)
        }
    }
}

@Composable
fun ReleaseDateSection(
    modifier: Modifier = Modifier, releaseDate: String, isLoading: Boolean
) {
    if (!isLoading) {
        if (releaseDate.isNotEmpty()) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .height(24.dp)
                        .background(
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(6.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = "Release Date:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                Text(
                    text = formatDate(releaseDate),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Spacer(height = 16.dp)
        }
    }
}

@Composable
fun RuntimeSection(
    modifier: Modifier = Modifier,
    runtime: String,
    isLoading: Boolean,
) {
    if (!isLoading && runtime.isNotEmpty()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.AccessTime,
                contentDescription = "AccessTime",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = runtime,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )
        }
    }
    Spacer(height = 16.dp)
}

@Composable
fun StatusBudgetRevenueRow(
    status: String, budget: String, revenue: String, borderColor: Color, isLoading: Boolean
) {
    if (!isLoading) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            DividerLine(borderColor)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoColumn(title = status, label = "Status", modifier = Modifier.weight(1f))
                VerticalDivider(borderColor)

                InfoColumn(title = budget, label = "Budget", modifier = Modifier.weight(1f))
                VerticalDivider(borderColor)

                InfoColumn(title = revenue, label = "Revenue", modifier = Modifier.weight(1f))
            }
            DividerLine(borderColor)
        }
        Spacer(height = 16.dp)
    }
}

@Composable
fun InfoColumn(
    title: String, label: String, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DividerLine(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color)
    )
}

@Composable
fun VerticalDivider(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(color)
    )
}

@Composable
fun SocialSection(
    modifier: Modifier = Modifier, social: Social?, homepage: String, isLoading: Boolean
) {
    if (!isLoading && social != null) {
        val instagramUrl =
            if (social.instagramId.isNotEmpty()) "https://www.instagram.com/${social.instagramId}/" else ""
        val facebookUrl =
            if (social.facebookId.isNotEmpty()) "https://www.facebook.com/${social.facebookId}/" else ""
        val twitterUrl =
            if (social.twitterId.isNotEmpty()) "https://twitter.com/${social.twitterId}" else ""
        val imdbUrl =
            if (social.imdbId.isNotEmpty()) "https://www.imdb.com/title/${social.imdbId}/" else ""
        val wikidataUrl =
            if (social.wikidataId.isNotEmpty()) "https://www.wikidata.org/wiki/${social.wikidataId}" else ""
        val homepageUrl = homepage.ifEmpty { "" }

        val socials = listOf(
            instagramUrl to R.drawable.instagram,
            facebookUrl to R.drawable.facebook,
            twitterUrl to R.drawable.x,
            imdbUrl to R.drawable.imdb,
            wikidataUrl to R.drawable.wikipedia,
            homepageUrl to R.drawable.homepage,
        )

        LazyRow(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(socials) { social ->
                SocialItem(
                    url = social.first, icon = social.second
                )
            }
        }
        Spacer(height = 16.dp)
    }
}

@Composable
fun SocialItem(url: String, icon: Int) {
    val context = LocalContext.current

    val iconColor = ColorFilter.tint(
        MaterialTheme.colorScheme.onBackground.copy(alpha = .85f)
    )
    if (url.isNotEmpty()) {
        Image(
            painter = painterResource(icon),
            contentDescription = url,
            colorFilter = iconColor,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                })
        Spacer(width = 16.dp)
    }
}

@Composable
fun PlaceholderBox(
    modifier: Modifier = Modifier,
    width: Dp = 64.dp,
    height: Dp = 20.dp,
    cornerRadius: Dp = 4.dp,
    placeholderColor: Color = MaterialTheme.colorScheme.onBackground.copy(.05f)
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(placeholderColor)
    )
}

@Composable
fun FavoriteToggleButton(
    favourite: Boolean,
    onToggle: () -> Unit,
    placeHolderColor: Color,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
) {
    val iconTint =
        if (isLoading) placeHolderColor else if (favourite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
            alpha = .75f
        )
    val scale = remember { Animatable(1f) }

    LaunchedEffect(favourite) {
        if (favourite) {
            scale.animateTo(
                targetValue = 1.2f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
                )
            )
            scale.animateTo(
                targetValue = 1f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        } else {
            scale.snapTo(1f)
        }
    }

    Box(
        modifier = modifier
            .size(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                onToggle()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (favourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            contentDescription = "Favourite",
            tint = iconTint,
            modifier = Modifier.scale(scale.value)
        )
    }
}











