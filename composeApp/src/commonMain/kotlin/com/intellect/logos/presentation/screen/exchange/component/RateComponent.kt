package com.intellect.logos.presentation.screen.exchange.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.common.presentation.ui.placeholder.PlaceholderBox
import com.intellect.logos.domain.model.exchange.Rate

@Composable
fun RateComponent(
    rate: Rate,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    PlaceholderBox(
        isLoading = isLoading,
        width = 100.dp,
        height = 20.dp,
        modifier = modifier
    ) {
        Text(
            text = buildString {
                append("1 ${rate.assets.first}")
                append(" = ")
                append("${rate.volume} ${rate.assets.second}")
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
    }
}