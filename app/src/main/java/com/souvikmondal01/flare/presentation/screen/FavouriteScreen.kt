package com.souvikmondal01.flare.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvikmondal01.flare.R
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.presentation.compoment.ImageCard
import com.souvikmondal01.flare.presentation.compoment.Spacer
import com.souvikmondal01.flare.presentation.compoment.StatusBarColor
import com.souvikmondal01.flare.presentation.viewmodel.MovieViewModel
import com.souvikmondal01.flare.util.Common.formatMinutesToDuration

@Composable
fun FavouriteScreen(
    onMovieClick: (Int, MediaType) -> Unit = { _, _ -> },
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    StatusBarColor()
    LaunchedEffect(Unit) {
        movieViewModel.getAllMedia()
    }
    val movieState by movieViewModel.movieState.collectAsStateWithLifecycle()
    val movies = movieState.localDBMovies

    var showDialog by remember { mutableStateOf(false) }
    var id by remember { mutableIntStateOf(0) }

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
        ) {
            Text(
                text = "Watchlist",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        // Top app-bar end

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            movies?.let {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(movies) { movie ->
                        val mediaType =
                            if (movie.mediaType == MediaType.Movie.type) MediaType.Movie else MediaType.Tv
                        val movieInfo = listOf(
                            movie.originalLanguage.capitalize(Locale.current),
                            movie.certification.takeIf { it != "N/A" }.orEmpty(),
                            movie.releaseDate.substring(0, 4),
                            formatMinutesToDuration(movie.runtime)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onBackground.copy(.05f))
                                .clickable {
                                    onMovieClick(
                                        movie.id,
                                        mediaType
                                    )
                                }
                                .padding(16.dp)
                        ) {
                            ImageCard(
                                imagePath = movie.posterPath,
                                contentDescription = movie.title,
                                onClick = { onMovieClick(movie.id, mediaType) }
                            )
                            Spacer(width = 16.dp)
                            Column(
                                modifier = Modifier.height(152.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column {
                                    Text(
                                        text = movie.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(height = 12.dp)
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        movieInfo.forEach {
                                            if (it.isNotEmpty()) {
                                                Text(
                                                    text = it,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = MaterialTheme.colorScheme.onBackground.copy(
                                                        alpha = .85f
                                                    ),
                                                    modifier = Modifier
                                                        .border(
                                                            width = .5.dp,
                                                            color = MaterialTheme.colorScheme.onBackground.copy(
                                                                alpha = .85f
                                                            ),
                                                            shape = RoundedCornerShape(6.dp)
                                                        )
                                                        .background(
                                                            MaterialTheme.colorScheme.onBackground.copy(
                                                                0.1f
                                                            )
                                                        )
                                                        .padding(
                                                            horizontal = 8.dp,
                                                            vertical = 2.dp
                                                        )
                                                )

                                            }
                                        }
                                    }

                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "%.1f".format(movie.voteAverage / 2),
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            contentDescription = "Star",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(start = 2.dp)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable {
                                                showDialog = true
                                                id = movie.id
                                            }
                                            .padding(vertical = 6.dp, horizontal = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.bookmark2),
                                            contentDescription = null,
                                        )
                                    }

                                }

                            }
                        }
                    }
                }

                this@Column.AnimatedVisibility(
                    visible = movies.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(R.drawable.empty),
                        contentDescription = null,
                        modifier = Modifier
                            .width(104.dp),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                    )
                }

                // Dialog
                DeleteConfirmationDialog(
                    showDialog = showDialog,
                    onConfirm = {
                        // Handle delete action
                        movieViewModel.deleteMediaById(id)
                        showDialog = false
                    },
                    onDismiss = {
                        // Close the dialog
                        showDialog = false
                    }
                )

            }
        }
    }
}


@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Delete Item",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete this item?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            textContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .85f),
        )
    }
}
