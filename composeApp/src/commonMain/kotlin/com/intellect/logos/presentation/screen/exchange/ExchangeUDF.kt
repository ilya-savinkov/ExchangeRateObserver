package com.intellect.logos.presentation.screen.exchange

import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.Rate
import com.intellect.logos.presentation.screen.exchange.model.AssetType
import com.intellect.logos.presentation.screen.exchange.model.Key
import com.intellect.logos.presentation.udf.UDF

object ExchangeUDF {
    data class State(
        val isLoadingAssets: Boolean,
        val isLoadingRate: Boolean,
        val baseAsset: Asset,
        val quoteAsset: Asset,
        val rate: Rate
    ) : UDF.State

    sealed interface Action : UDF.Action {
        data class TapAsset(val asset: Asset, val assetType: AssetType) : Action
        data class TapKey(val key: Key) : Action
        data object Swap : Action
    }

    interface Event : UDF.Event
}