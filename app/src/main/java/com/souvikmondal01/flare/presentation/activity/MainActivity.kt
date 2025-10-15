package com.souvikmondal01.flare.presentation.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.souvikmondal01.flare.R
import com.souvikmondal01.flare.presentation.screen.CreditsScreen
import com.souvikmondal01.flare.presentation.screen.FavouriteScreen
import com.souvikmondal01.flare.presentation.screen.HomeScreen
import com.souvikmondal01.flare.presentation.screen.MovieGridScreen
import com.souvikmondal01.flare.presentation.screen.MovieScreen
import com.souvikmondal01.flare.presentation.screen.Screen
import com.souvikmondal01.flare.presentation.screen.SearchScreen
import com.souvikmondal01.flare.ui.theme.PhaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        Firebase.analytics
        setContent {
            PhaseTheme {
                var selectedBottomNavDestinationIndex by rememberSaveable { mutableIntStateOf(0) }

                Scaffold(bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp, topEnd = 20.dp
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.65f),
                                    ),
                                )
                            ), containerColor = Color.Transparent
                    ) {
                        bottomNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = index == selectedBottomNavDestinationIndex,
                                onClick = {
                                    selectedBottomNavDestinationIndex = index
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(item.second),
                                        contentDescription = item.first
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = .8f
                                    ),
                                    indicatorColor = Color.Transparent,
                                )
                            )

                        }
                    }
                }) {
                    Box(
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding())
                    ) {
                        ContentScreen(
                            selectedIndex = selectedBottomNavDestinationIndex
                        )
                    }
                }

                BackHandler {
                    if (selectedBottomNavDestinationIndex > 0) {
                        selectedBottomNavDestinationIndex = 0
                    } else {
                        finish()
                    }
                }
            }
        }
    }
}

val bottomNavigationItems = listOf(
    "Home" to R.drawable.home, "Search" to R.drawable.search, "Favourite" to R.drawable.bookmark
)

@Composable
fun ContentScreen(
    selectedIndex: Int = 0,
) {
    val homeBackStack = rememberNavBackStack(Screen.HomeScreen)
    val searchBackStack = rememberNavBackStack(Screen.SearchScreen)
    val favouriteBackStack = rememberNavBackStack(Screen.FavouriteScreen)

    when (selectedIndex) {
        0 -> HomeNavigationRoot(homeBackStack)
        1 -> SearchNavigationRoot(searchBackStack)
        2 -> FavouriteNavigationRoot(favouriteBackStack)
    }
}

@Composable
fun HomeNavigationRoot(backStack: NavBackStack<NavKey>) {
    val activity = LocalView.current.context as? Activity

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ), entryProvider = { key ->
            when (key) {
                is Screen.HomeScreen -> {
                    NavEntry(key) {
                        HomeScreen(onMovieClick = { movieId, category ->
                            backStack.add(Screen.MovieDetailScreen(movieId, category.mediaType))
                        }, onBackClick = {
                            activity?.finish()
                        }, onMoreClick = {
                            backStack.add(Screen.MovieGridScreen(it))
                        })
                    }
                }

                is Screen.MovieDetailScreen -> {
                    NavEntry(key) {
                        MovieScreen(
                            id = key.id,
                            mediaType = key.mediaType,
                            onSimilarMovieClick = { movieId, mediaType ->
                                backStack.add(Screen.MovieDetailScreen(movieId, mediaType))
                            },
                            onBackClick = {
                                backStack.removeLastOrNull()
                            },
                            onFullCreditsClick = { movieId, mediaType ->
                                backStack.add(Screen.CreditsScreen(movieId, mediaType))
                            })
                    }
                }

                is Screen.MovieGridScreen -> {
                    NavEntry(key) {
                        MovieGridScreen(category = key.category, onMovieClick = { id, mediaType ->
                            backStack.add(
                                Screen.MovieDetailScreen(
                                    id = id, mediaType = mediaType
                                )
                            )
                        }, onBackClick = {
                            backStack.removeLastOrNull()
                        })
                    }
                }

                is Screen.CreditsScreen -> {
                    NavEntry(key) {
                        CreditsScreen(
                            id = key.id, mediaType = key.mediaType, onBackClick = {
                                backStack.removeLastOrNull()
                            })
                    }
                }

                else -> throw RuntimeException("Invalid NavKey.")
            }

        })
}

@Composable
fun SearchNavigationRoot(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ), entryProvider = { key ->
            when (key) {
                is Screen.SearchScreen -> {
                    NavEntry(key) {
                        SearchScreen(onMovieClick = { movieId, mediaType ->
                            backStack.add(Screen.MovieDetailScreen(movieId, mediaType))
                        })
                    }
                }

                is Screen.MovieDetailScreen -> {
                    NavEntry(key) {
                        MovieScreen(
                            id = key.id,
                            mediaType = key.mediaType,
                            onSimilarMovieClick = { movieId, mediaType ->
                                backStack.add(Screen.MovieDetailScreen(movieId, mediaType))
                            },
                            onBackClick = {
                                backStack.removeLastOrNull()
                            },
                            onFullCreditsClick = { movieId, mediaType ->
                                backStack.add(Screen.CreditsScreen(movieId, mediaType))
                            })
                    }
                }

                is Screen.CreditsScreen -> {
                    NavEntry(key) {
                        CreditsScreen(
                            id = key.id, mediaType = key.mediaType, onBackClick = {
                                backStack.removeLastOrNull()
                            })
                    }
                }

                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}

@Composable
fun FavouriteNavigationRoot(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ), entryProvider = { key ->
            when (key) {
                is Screen.FavouriteScreen -> {
                    NavEntry(key) {
                        FavouriteScreen(onMovieClick = { movieId, mediaType ->
                            backStack.add(Screen.MovieDetailScreen(movieId, mediaType))
                        })
                    }
                }

                is Screen.MovieDetailScreen -> {
                    NavEntry(key) {
                        MovieScreen(
                            id = key.id,
                            mediaType = key.mediaType,
                            onSimilarMovieClick = { movieId, mediaType ->
                                backStack.add(Screen.MovieDetailScreen(movieId, mediaType))
                            },
                            onBackClick = {
                                backStack.removeLastOrNull()
                            },
                            onFullCreditsClick = { movieId, mediaType ->
                                backStack.add(Screen.CreditsScreen(movieId, mediaType))
                            })
                    }
                }

                is Screen.CreditsScreen -> {
                    NavEntry(key) {
                        CreditsScreen(
                            id = key.id, mediaType = key.mediaType, onBackClick = {
                                backStack.removeLastOrNull()
                            })
                    }
                }

                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}