package com.intellect.logos.presentation.screen.exchange

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.exchange.Rate
import com.intellect.logos.domain.model.exchange.Volume
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF
import com.intellect.logos.presentation.screen.exchange.component.exchange.ExchangeUDF.Event
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardUDF
import kotlinx.coroutines.flow.MutableStateFlow

object ExchangeViewModelUDF {
    data object State : UDF.State

    data class Model(
        override val isKeyboardEnabled: MutableStateFlow<Boolean>,
        override val isLoadingAssets: MutableStateFlow<Boolean>,
        override val baseAsset: MutableStateFlow<Asset>,
        override val quoteAsset: MutableStateFlow<Asset>,
        override val volume: MutableStateFlow<Volume>,
        override val convertedVolume: MutableStateFlow<String>,
        override val isLoadingRate: MutableStateFlow<Boolean>,
        override val rate: MutableStateFlow<Rate>
    ) : UDF.Model,
        KeyboardUDF.Model,
        ExchangeUDF.Model

    sealed interface Action : UDF.Action {
        data object OpenSettings : Action
    }

    sealed interface Event : UDF.Event {
        data object FailedToLoadAssets : Event
    }
}