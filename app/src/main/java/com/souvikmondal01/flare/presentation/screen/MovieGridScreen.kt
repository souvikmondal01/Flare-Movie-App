package com.souvikmondal01.flare.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.presentation.compoment.MovieGrid
import com.souvikmondal01.flare.presentation.compoment.StatusBarColor
import com.souvikmondal01.flare.presentation.viewmodel.MovieViewModel
import com.souvikmondal01.flare.util.Category

@Composable
fun MovieGridScreen(
    category: Category,
    onBackClick: () -> Unit = {},
    onMovieClick: (Int, MediaType) -> Unit = { _, _ -> },
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    StatusBarColor()

    LaunchedEffect(category) {
        movieViewModel.loadMoviesByCategory(category)
    }

    val movieState by movieViewModel.movieState.collectAsStateWithLifecycle()
    val movies = movieState.moviesState[category]?.movies.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Top app-bar start
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.padding(6.dp)
                )
            }
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
        // Top app-bar end

        MovieGrid(
            movies = movies,
            onMovieClick = { id, mediaType ->
                onMovieClick(id, category.mediaType)
            },
            paginate = { movieViewModel.paginate(category) }
        )

    }

    BackHandler {
        onBackClick()
    }
}



