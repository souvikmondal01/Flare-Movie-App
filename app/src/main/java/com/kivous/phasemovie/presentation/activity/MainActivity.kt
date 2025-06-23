package com.kivous.phasemovie.presentation.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.kivous.phasemovie.R
import com.kivous.phasemovie.presentation.screen.FavouriteScreen
import com.kivous.phasemovie.presentation.screen.HomeScreen
import com.kivous.phasemovie.presentation.screen.MovieDetailsScreen
import com.kivous.phasemovie.presentation.screen.MovieListScreen
import com.kivous.phasemovie.presentation.screen.Screen
import com.kivous.phasemovie.presentation.screen.SearchScreen
import com.kivous.phasemovie.ui.theme.PhaseTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            var selectedIndex by remember { mutableIntStateOf(0) }

            PhaseTheme {
                Scaffold(bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp, topEnd = 20.dp
                                )
                            )
                            .graphicsLayer { alpha = 0.95f }
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.65f),
                                    ),
                                )
                            ), tonalElevation = 0.dp, containerColor = Color.Transparent) {
                        bottomNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = index == selectedIndex, onClick = {
                                    selectedIndex = index
                                }, icon = {
                                    Icon(
                                        painter = painterResource(item.second),
                                        contentDescription = item.first
                                    )
                                }, colors = NavigationBarItemDefaults.colors(
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
                            selectedIndex = selectedIndex
                        )
                    }
                }
            }
        }
    }
}

val bottomNavigationItems: List<Pair<String, Int>> = listOf(
    "Home" to R.drawable.home, "Search" to R.drawable.search, "Favourite" to R.drawable.bookmark
)

@Composable
fun ContentScreen(
    selectedIndex: Int = 0,
) {
    when (selectedIndex) {
        0 -> NavigationRoot()
        1 -> SearchScreen()
        2 -> FavouriteScreen()
    }
}

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Screen.HomeScreen)
    val activity = LocalView.current.context as? Activity

    NavDisplay(
        backStack = backStack, entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ), entryProvider = { key ->
            when (key) {
                is Screen.HomeScreen -> {
                    NavEntry(key) {
                        HomeScreen(
                            onMovieClick = {
                                backStack.add(Screen.MovieDetailScreen(it.id))
                            },
                            onBackClick = {
                                activity?.finish()
                            },
                            onMoreClick = {
                                backStack.add(Screen.MovieListScreen(it))
                            }
                        )
                    }
                }

                is Screen.MovieDetailScreen -> {
                    NavEntry(key) {
                        MovieDetailsScreen(
                            movieId = key.movieId,
                            onSimilarMovieClick = {
                                backStack.add(Screen.MovieDetailScreen(it))
                            }
                        )
                    }
                }

                is Screen.MovieListScreen -> {
                    NavEntry(key) {
                        MovieListScreen(
                            category = key.category,
                            onMovieClick = {
                                backStack.add(Screen.MovieDetailScreen(it))
                            },
                            onBackClick = {
                                backStack.removeLastOrNull()
                            })
                    }
                }

                else -> throw RuntimeException("Invalid NavKey.")
            }

        })
}