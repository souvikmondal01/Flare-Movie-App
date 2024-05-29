package com.kivous.phasemovie.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kivous.phasemovie.ui.theme.NunitoBold

class FavouriteScreen : Screen {
    @Composable
    override fun Content() {
        FavouriteScreenUI()
    }
}

@Composable
fun FavouriteScreenUI() {

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Black)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Soon...",
            fontFamily = NunitoBold,
            fontSize = 18.sp
        )
    }
}