package com.kivous.phasemovie.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.kivous.phasemovie.domain.model.Movie
import com.kivous.phasemovie.presentation.compoment.ChangeStatusBarColor
import com.kivous.phasemovie.presentation.compoment.MovieRowSection
import com.kivous.phasemovie.presentation.viewmodel.MovieListViewModel
import com.kivous.phasemovie.ui.theme.NunitoBold
import com.kivous.phasemovie.ui.theme.PoppinsBold
import com.kivous.phasemovie.ui.theme.Red
import com.kivous.phasemovie.util.Category
import com.kivous.phasemovie.util.Common
import com.kivous.phasemovie.util.Common.getLanguageName
import com.kivous.phasemovie.util.getAverageColor

@Composable
fun MovieDetailsScreen(
    movieId: Int = 0
) {
    val movie = Movie(
        adult = false,
        backdrop_path = "/a3F9cXjRH488qcOqFmFZwqawBMU.jpg",
        genre_ids = listOf(16, 28, 878),
        original_language = "en",
        original_title = "Predator: Killer of Killers",
        overview = "While three of the fiercest warriors in human history—a Viking raider, a ninja in feudal Japan, and a WWII pilot—are killers in their own right, they are merely prey for their new opponent: the ultimate killer of killers.",
        popularity = 587.5582,
        poster_path = "/lbimIPTVsSlnmqSW5ngEsUxtHLM.jpg",
        release_date = "2025-06-05",
        title = "Predator: Killer of Killers",
        video = false,
        vote_average = 7.991,
        vote_count = 469,
        id = 1376434,
        category = "now_playing"
    )

    var avgColor by remember {
        mutableStateOf(Color.Black)
    }

    val scrollState = rememberScrollState()

    ChangeStatusBarColor(scrollState.value, avgColor)

    val imageStateBackDrop = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(movie.backdrop_path)
            .size(Size.ORIGINAL).build()
    ).state

    val imageStatePoster = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(movie.poster_path)
            .size(Size.ORIGINAL).build()
    ).state

    if (imageStatePoster is AsyncImagePainter.State.Success) {
        avgColor = getAverageColor(
            imageBitmap = imageStatePoster.result.drawable.toBitmap().asImageBitmap()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {

            if (imageStateBackDrop is AsyncImagePainter.State.Error || imageStateBackDrop is AsyncImagePainter.State.Loading) Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xff0a0a0a))
            )

            if (imageStateBackDrop is AsyncImagePainter.State.Success) {
                Image(
                    painter = imageStateBackDrop.painter,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(4.dp),
                    contentScale = ContentScale.Crop,
                    alpha = .60f
                )
            }

            // Shadow effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            0.0f to Color.Black,
                            0.3f to Color.Transparent,
                            0.5f to Color.Transparent,
                            0.9f to Color.Black.copy(alpha = .85f),
                            0.97f to Color.Black.copy(alpha = .95f),
                            1f to Color.Black,
                            start = Offset(0.0f, 20.0f),
                            end = Offset(0.0f, 640.0f)
                        )
                    )
            )

            if (imageStatePoster is AsyncImagePainter.State.Success) {
                Image(
                    painter = imageStatePoster.painter,
                    contentDescription = "",
                    modifier = Modifier
                        .height(176.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(12.dp)),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.title,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color(0xffF0E1E1),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontFamily = PoppinsBold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = movie.release_date.substring(0, 4),
                color = Color(0xff958B8B),
                fontSize = 14.sp,
                fontFamily = NunitoBold
            )

            Text(
                text = "•",
                color = Color(0xff958B8B),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = getLanguageName(movie.original_language),
                color = Color(0xff958B8B),
                fontSize = 14.sp,
                fontFamily = NunitoBold
            )

            Text(
                text = "•",
                color = Color(0xff958B8B),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Red)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.1f", movie.vote_average / 2),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = NunitoBold
                    )

                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )

                }

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.genre_ids.joinToString(separator = "  |  ") {
                Common.getGenres(it)
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
                .horizontalScroll(rememberScrollState()),
            color = Color(0xffF0E1E1),
            fontSize = 16.sp,
            fontFamily = NunitoBold
        )

        if (movie.overview.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = movie.overview,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color(0xff958B8B),
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(8.dp))

        val movieListViewModel: MovieListViewModel = hiltViewModel()
        val movieListState = movieListViewModel.movieListState.collectAsState().value

        LaunchedEffect(key1 = movieId) {
            movieListViewModel.getSimilarMovieList(movieId.toString())
        }

        Spacer(modifier = Modifier.height(64.dp))

        if (movieListState.similarMovieList.isNotEmpty()) {
            MovieRowSection(
                category = Category.SIMILAR,
                movieList = movieListState.similarMovieList,
                onMoreClick = {

                },
                onMovieClick = {
                },
                more = false
            )
        }

        Box(modifier = Modifier.height(124.dp))

    }


}