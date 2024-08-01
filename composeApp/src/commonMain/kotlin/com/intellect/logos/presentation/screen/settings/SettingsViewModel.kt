package com.intellect.logos.presentation.screen.settings

import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.presentation.screen.settings.StateUDF.Action
import com.intellect.logos.presentation.screen.settings.StateUDF.Event
import com.intellect.logos.presentation.screen.settings.StateUDF.State

class SettingsViewModel(
    private val router: SettingsRouter
) : BaseViewModel<State, Action, Event>(
    initialState = State()
) {
    companion object {
        const val ROUTE = "settings"
    }

    override suspend fun reduce(action: Action) {
        when (action) {
            Action.Close -> router.close()
        }
    }
}