package com.intellect.logos.presentation.screen.exchange.component.keyboard

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.model.exchange.Volume
import kotlinx.coroutines.flow.MutableStateFlow

object KeyboardUDF {
    interface Model : UDF.Model {
        val isKeyboardEnabled: MutableStateFlow<Boolean>
        val baseAsset: MutableStateFlow<Asset>
        val volume: MutableStateFlow<Volume>
    }

    data object State : UDF.State

    sealed interface Action : UDF.Action {
        data class TapKey(val key: Key) : Action
    }

    sealed interface Event : UDF.Event
}