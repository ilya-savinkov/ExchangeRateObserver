package com.intellect.logos.presentation.screen.exchange.component.exchange.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.common.presentation.ui.placeholder.SimplePlaceholderBox
import com.intellect.logos.presentation.screen.exchange.component.exchange.model.RateState

@Composable
fun RateComposable(
    rateState: RateState,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = rateState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentKey = { it::class },
        modifier = modifier
    ) { state ->
        when (state) {
            is RateState.Data -> Data(state)
            is RateState.Loading -> Loading()
        }
    }
}

@Composable
private fun Loading() {
    SimplePlaceholderBox(
        width = 100.dp,
        height = 20.dp,
    )
}

@Composable
private fun Data(state: RateState.Data) {
    Text(
        text = "1 ${state.baseAssetName} = ${state.rate} ${state.quoteAssetName}",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
    )
}