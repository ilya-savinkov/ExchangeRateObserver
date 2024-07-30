package com.intellect.logos.presentation.screen.exchange

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.Key
import com.intellect.logos.domain.model.Rate

object ExchangeUDF {
    data class State(
        val isLoadingAssets: Boolean,
        val isLoadingRate: Boolean,
        val baseAsset: Asset,
        val quoteAsset: Asset,
        val volume: String,
        val convertedVolume: String,
        // TODO Использовать примитивные типы
        val rate: Rate
    ) : UDF.State

    sealed interface Action : UDF.Action {
        data class TapAsset(val asset: Asset, val type: Asset.Type) : Action
        data class TapKey(val key: Key) : Action
        data object Swap : Action
    }

    interface Event : UDF.Event {
        data object FailedToLoadAssets : Event
        data object FailedToLoadRate : Event
    }
}