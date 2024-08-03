package com.intellect.logos.presentation.screen.settings

import androidx.lifecycle.viewModelScope
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.usecase.settings.ChangeThemeUseCase
import com.intellect.logos.domain.usecase.settings.GetDefaultThemeUseCase
import com.intellect.logos.domain.usecase.settings.GetThemeStateFlowUseCase
import com.intellect.logos.presentation.screen.settings.StateUDF.Action
import com.intellect.logos.presentation.screen.settings.StateUDF.Event
import com.intellect.logos.presentation.screen.settings.StateUDF.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsViewModel(
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val router: SettingsRouter,
    getDefaultThemeUseCase: GetDefaultThemeUseCase,
    getThemeStateFlowUseCase: GetThemeStateFlowUseCase
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        theme = getDefaultThemeUseCase()
    )
) {
    companion object {
        const val ROUTE = "settings"
    }

    init {
        getThemeStateFlowUseCase().onEach { theme ->
            setState {
                copy(theme = theme)
            }
        }.launchIn(viewModelScope)
    }

    override suspend fun reduce(action: Action) = when (action) {
        is Action.ChangeTheme -> changeTheme(action.theme)
        Action.Close -> router.close()
    }

    private suspend fun changeTheme(theme: Theme) {
        changeThemeUseCase(theme)
    }
}