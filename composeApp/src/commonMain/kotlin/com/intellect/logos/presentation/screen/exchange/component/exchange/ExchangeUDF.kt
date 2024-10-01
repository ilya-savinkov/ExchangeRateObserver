package com.intellect.logos.presentation.screen.exchange.component.exchange

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.presentation.screen.exchange.component.exchange.model.AssetState
import com.intellect.logos.presentation.screen.exchange.component.exchange.model.RateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ExchangeUDF {
    interface Model : UDF.Model {
        val isLoadingAssets: MutableStateFlow<Boolean>
        val baseAsset: MutableStateFlow<Asset>
        val quoteAsset: MutableStateFlow<Asset>
        val volume: MutableStateFlow<Volume>
        val isLoadingRate: MutableStateFlow<Boolean>
        val convertedVolume: MutableStateFlow<String>
        val rate: MutableStateFlow<Rate>
    }

    data class State(
        val baseAsset: StateFlow<AssetState>,
        val quoteAsset: StateFlow<AssetState>,
        val rate: StateFlow<RateState>
    ) : UDF.State

    sealed interface Action : UDF.Action {
        data class TapAsset(val type: Asset.Type) : Action
        data object Swap : Action
    }

    sealed interface Event : UDF.Event {
        data object FailedToLoadRate : Event
    }
}