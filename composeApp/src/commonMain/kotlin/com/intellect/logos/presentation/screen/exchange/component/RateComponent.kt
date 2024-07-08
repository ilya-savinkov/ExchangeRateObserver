package com.intellect.logos.presentation.screen.exchange.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellect.logos.domain.model.Rate
import com.intellect.logos.presentation.ui.placeholder.PlaceholderBox

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
                append("${rate.baseAsset.volume} ${rate.baseAsset.currency.code}")
                append(" = ")
                append("${rate.quoteAsset.volume} ${rate.quoteAsset.currency.code}")
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
    }
}