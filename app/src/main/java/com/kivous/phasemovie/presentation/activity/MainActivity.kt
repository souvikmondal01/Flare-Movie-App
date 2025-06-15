package com.kivous.phasemovie.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kivous.phasemovie.presentation.screen.FavouriteScreen
import com.kivous.phasemovie.presentation.screen.HomeScreen
import com.kivous.phasemovie.ui.theme.PhaseTheme
import com.kivous.phasemovie.ui.theme.Red
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            var selectedIndex by remember {
                mutableIntStateOf(0)
            }

            PhaseTheme {

                Scaffold(bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xff0a0a0a),
                    ) {
                        bottomNavigationItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = index == selectedIndex, onClick = {
                                    selectedIndex = index
                                }, icon = {
                                    Icon(
                                        imageVector = item.second, contentDescription = item.first
                                    )
                                }, colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Red,
                                    unselectedIconColor = Color(0xFF7D7D7D),
                                    indicatorColor = Color(0xff180E0E),
                                    selectedTextColor = Color(0xffFF4040),
                                    unselectedTextColor = Color(0xFF7D7D7D),
                                    disabledIconColor = Color.Transparent,
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

val bottomNavigationItems: List<Pair<String, ImageVector>> = listOf(
    "Home" to Icons.Rounded.Home, "Favourite" to Icons.Rounded.Bookmark
)

@Composable
fun ContentScreen(
    selectedIndex: Int = 0,
) {
    when (selectedIndex) {
        0 -> HomeScreen()
        1 -> FavouriteScreen()
    }
}