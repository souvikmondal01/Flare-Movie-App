package com.kivous.phasemovie.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.kivous.phasemovie.presentation.compoment.ChangeStatusBarColor
import com.kivous.phasemovie.presentation.compoment.MovieRow
import com.kivous.phasemovie.presentation.viewmodel.MovieViewModel
import com.kivous.phasemovie.ui.theme.NunitoBold
import com.kivous.phasemovie.ui.theme.PoppinsBold
import com.kivous.phasemovie.ui.theme.Red
import com.kivous.phasemovie.util.Category
import com.kivous.phasemovie.util.Common.getLanguageName
import com.kivous.phasemovie.util.Extension.logD
import com.kivous.phasemovie.util.getAverageColor

@Composable
fun MovieDetailsScreen(
    movieId: Int = 0,
    onBackClick: () -> Unit = {},
    onSimilarMovieClick: (Int) -> Unit = {},
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        movieViewModel.getMovieDetails(id = movieId.toString())
        movieViewModel.getMovieCredits(id = movieId.toString())
    }

    val movieState by movieViewModel.movieListState.collectAsStateWithLifecycle()
    val movie = movieState.movieDetails
    val cast = movieState.movieCredits?.cast

    var avgColor by remember {
        mutableStateOf(Color.Black)
    }

    val scrollState = rememberScrollState()

    ChangeStatusBarColor(scrollState = scrollState.value, color = avgColor)

    val imageStateBackDrop = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(movie?.backdropPath)
            .size(Size.ORIGINAL).build()
    ).state

    val imageStatePoster = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(movie?.posterPath)
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
            text = movie?.title ?: "",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color(0xffF0E1E1),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontFamily = PoppinsBold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        movie?.let {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Spacer(modifier = Modifier.width(8.dp))

                movie?.releaseDate?.substring(0, 4)?.let {
                    Text(
                        text = it,
                        color = Color(0xff958B8B),
                        fontSize = 14.sp,
                        fontFamily = NunitoBold
                    )
                }

                Text(
                    text = "•",
                    color = Color(0xff958B8B),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = getLanguageName(movie?.originalLanguage ?: ""),
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
                            text = String.format("%.1f", movie?.voteAverage?.div(2)),
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = movie?.genres?.joinToString(separator = "  |  ") {
            it.name
        } ?: "",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
                .horizontalScroll(rememberScrollState()),
            color = Color(0xffF0E1E1),
            fontSize = 16.sp,
            fontFamily = NunitoBold)

        movie?.overview?.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Text(
            text = movie?.overview ?: "",
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color(0xff958B8B),
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Justify
        )
//        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cast?.let {
                items(cast) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable{
                            logD(it.order)
                        }
                    ) {
                        AsyncImage(
                            model = it.profilePath,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                        )
                        Text(text = it.name)
                        Text(text = it.character)
                    }
                }
            }

        }

        val movieViewModel: MovieViewModel = hiltViewModel()
        val movieListState = movieViewModel.movieListState.collectAsState().value

        LaunchedEffect(key1 = movieId) {
            movieViewModel.getSimilarMovieList(movieId.toString())
        }

        Spacer(modifier = Modifier.height(64.dp))

        if (movieListState.similarMovieList.isNotEmpty()) {
            MovieRow(
                category = Category.SIMILAR,
                movieList = movieListState.similarMovieList,
                onMoreClick = {

                },
                onMovieClick = {
                    onSimilarMovieClick(it.id)
                },
                more = false
            )
        }

        Box(modifier = Modifier.height(124.dp))

    }


}