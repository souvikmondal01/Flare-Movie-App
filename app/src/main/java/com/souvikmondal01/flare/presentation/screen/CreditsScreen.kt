package com.souvikmondal01.flare.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ArrowBackIosNew
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvikmondal01.flare.domain.model.MediaType
import com.souvikmondal01.flare.presentation.compoment.ImageCard
import com.souvikmondal01.flare.presentation.compoment.StatusBarColor
import com.souvikmondal01.flare.presentation.state.LoadState
import com.souvikmondal01.flare.presentation.viewmodel.MovieViewModel

@Composable
fun CreditsScreen(
    id: Int,
    mediaType: MediaType,
    onBackClick: () -> Unit = {},
    movieViewModel: MovieViewModel = hiltViewModel(),
) {
    StatusBarColor()

    LaunchedEffect(Unit) { movieViewModel.getCredits(mediaType = mediaType, id = id) }
    val movieState by movieViewModel.movieState.collectAsStateWithLifecycle()
    val credits = movieState.credits
    val isLoading = movieState.creditsLoadState == LoadState.Loading && credits == null

    val cast = credits?.cast.orEmpty()
    val crew = credits?.crew.orEmpty()

    var isCastExpanded by remember { mutableStateOf(true) }
    var isCrewExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Top App Bar
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
        }

        if (!isLoading) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ExpandableSectionHeader(
                        title = "Cast",
                        expanded = isCastExpanded,
                        onToggle = { isCastExpanded = !isCastExpanded })
                }

                if (isCastExpanded) {
                    items(cast) { castItem ->
                        CreditItem(
                            name = castItem.name,
                            subtitle = castItem.character,
                            imagePath = castItem.profilePath
                        )
                    }
                }

                item {
                    ExpandableSectionHeader(
                        title = "Crew",
                        expanded = isCrewExpanded,
                        onToggle = { isCrewExpanded = !isCrewExpanded })
                }

                if (isCrewExpanded) {
                    items(crew) { crewItem ->
                        CreditItem(
                            name = crewItem.name,
                            subtitle = crewItem.department,
                            imagePath = crewItem.profilePath
                        )
                    }
                }
            }
        }
    }

    BackHandler {
        onBackClick()
    }

}

@Composable
fun ExpandableSectionHeader(title: String, expanded: Boolean, onToggle: () -> Unit) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "$title Rotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onToggle() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "Toggle $title",
            modifier = Modifier.rotate(rotationAngle)
        )
    }


}

@Composable
fun CreditItem(
    name: String, subtitle: String, imagePath: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ImageCard(
            imagePath = imagePath.orEmpty(),
            modifier = Modifier
                .height(136.dp)
                .width(108.dp),
            cornerRadius = 8.dp
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(
                subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f)
            )
        }
    }
}
