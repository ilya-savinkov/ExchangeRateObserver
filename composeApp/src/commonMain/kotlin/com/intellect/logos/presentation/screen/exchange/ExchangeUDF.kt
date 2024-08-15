package com.intellect.logos.presentation.screen.exchange

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.model.exchange.Volume

object ExchangeUDF {
    data class State(
        val isLoadingAssets: Boolean,
        val isLoadingRate: Boolean,
        val baseAsset: Asset,
        val quoteAsset: Asset,
        val volume: Volume,
        val convertedVolume: String,
        val rate: Rate
    ) : UDF.State

    sealed interface Action : UDF.Action {
        data class TapAsset(val type: Asset.Type) : Action
        data class TapKey(val key: Key) : Action
        data object Swap : Action
        data object OpenSettings : Action
    }

    interface Event : UDF.Event {
        data object FailedToLoadAssets : Event
        data object FailedToLoadRate : Event
    }
}