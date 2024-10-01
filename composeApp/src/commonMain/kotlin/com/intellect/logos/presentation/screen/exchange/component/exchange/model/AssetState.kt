package com.intellect.logos.presentation.screen.exchange.component.exchange.model

sealed interface AssetState {
    data object Loading : AssetState

    data class Data(
        val name: String,
        val icon: String,
        val volume: String
    ) : AssetState
}