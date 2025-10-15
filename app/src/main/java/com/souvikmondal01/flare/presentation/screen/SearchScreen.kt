package com.souvikmondal01.flare.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvikmondal01.flare.R
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.presentation.compoment.MovieGrid
import com.souvikmondal01.flare.presentation.compoment.StatusBarColor
import com.souvikmondal01.flare.presentation.state.LoadState
import com.souvikmondal01.flare.presentation.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    onMovieClick: (Int, MediaType) -> Unit = { _, _ -> },
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    StatusBarColor()
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(query) {
        movieViewModel.clearSearch()
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            return@LaunchedEffect
        }
        delay(500)
        movieViewModel.getSearchMovies(trimmed)
    }

    val searchMovieState by movieViewModel.searchMovieState.collectAsStateWithLifecycle()
    val isLoading = searchMovieState.loadState == LoadState.Loading
    val movies = searchMovieState.movies

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground.copy(.1f), CircleShape)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onBackground.copy(.5f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .padding(vertical = 16.dp),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        innerTextField()
                    })

                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onBackground.copy(.5f)
                        )
                    }
                }
            }
        }

        if (movies.isNotEmpty()) {
            MovieGrid(movies = movies, onMovieClick = { id, mediaType ->
                onMovieClick(id, mediaType)
            }, paginate = { movieViewModel.searchPaginate(query) })
        }

    }
    if (isLoading && movies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

}