package com.intellect.logos.presentation.screen.exchange.component.keyboard

import com.intellect.logos.common.presentation.udf.BaseComponent
import com.intellect.logos.domain.model.exchange.Key
import com.intellect.logos.domain.usecase.volume.UpdateVolumeUseCase
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardUDF.Action
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardUDF.Event
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardUDF.Model
import com.intellect.logos.presentation.screen.exchange.component.keyboard.KeyboardUDF.State
import kotlinx.coroutines.CoroutineScope

class KeyboardComponent(
    private val updateVolumeUseCase: UpdateVolumeUseCase,
    private val model: Model,
    coroutineScope: CoroutineScope
) : BaseComponent<State, Model, Action, Event>(
    coroutineScope = coroutineScope
) {
    override val state: State = State

    override suspend fun reduce(action: Action) {
        when (action) {
            is Action.TapKey -> onKeyTap(action.key)
        }
    }

    private suspend fun onKeyTap(key: Key) {
        if (!model.isKeyboardEnabled.value) return

        updateVolumeUseCase(
            asset = model.baseAsset.value,
            volume = model.volume.value,
            key = key
        )
    }
}