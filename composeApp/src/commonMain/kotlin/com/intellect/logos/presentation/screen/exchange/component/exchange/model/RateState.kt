package com.intellect.logos.presentation.screen.exchange.component.exchange.model

sealed interface RateState {
    data object Loading : RateState

    data class Data(
        val baseAssetName: String,
        val quoteAssetName: String,
        val rate: String
    ) : RateState
}