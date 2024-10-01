package com.intellect.logos.presentation.screen.exchange.component.exchange.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.intellect.logos.common.presentation.ui.placeholder.SimplePlaceholderBox
import com.intellect.logos.presentation.screen.exchange.component.exchange.model.AssetState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AssetInputComposable(
    assetState: AssetState,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = assetState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentKey = { it::class },
        modifier = modifier
    ) { state ->
        when (state) {
            is AssetState.Data -> Data(
                state = state,
                onClick = onClick,
                animatedVisibilityScope = animatedVisibilityScope
            )

            AssetState.Loading -> Loading()
        }
    }
}

@Composable
private fun Loading() {
    SimplePlaceholderBox(
        height = 100.dp
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Data(
    state: AssetState.Data,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KamelImage(
                resource = asyncPainterResource(state.icon),
                contentDescription = state.name,
                modifier = Modifier
                    .size(48.dp)
                    .sharedElement(
                        state = rememberSharedContentState(state.icon),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )

            Text(
                text = state.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(state.name),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // TODO Уменьшать поле при большом количестве символов
        Text(
            text = state.volume,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}