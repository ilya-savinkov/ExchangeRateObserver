package com.intellect.logos.presentation.screen.settings

import androidx.lifecycle.viewModelScope
import com.intellect.logos.common.presentation.udf.BaseViewModel
import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.model.settings.Theme
import com.intellect.logos.domain.usecase.settings.language.ChangeLanguageUseCase
import com.intellect.logos.domain.usecase.settings.language.GetDefaultLanguageUseCase
import com.intellect.logos.domain.usecase.settings.language.GetLanguageStateFlowUseCase
import com.intellect.logos.domain.usecase.settings.theme.ChangeThemeUseCase
import com.intellect.logos.domain.usecase.settings.theme.GetDefaultThemeUseCase
import com.intellect.logos.domain.usecase.settings.theme.GetThemeStateFlowUseCase
import com.intellect.logos.presentation.screen.settings.StateUDF.Action
import com.intellect.logos.presentation.screen.settings.StateUDF.Event
import com.intellect.logos.presentation.screen.settings.StateUDF.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsViewModel(
    private val changeThemeUseCase: ChangeThemeUseCase,
    private val changeLanguageUseCase: ChangeLanguageUseCase,
    private val router: SettingsRouter,
    getDefaultThemeUseCase: GetDefaultThemeUseCase,
    getThemeStateFlowUseCase: GetThemeStateFlowUseCase,
    getDefaultLanguageUseCase: GetDefaultLanguageUseCase,
    getLanguageStateFlowUseCase: GetLanguageStateFlowUseCase,
) : BaseViewModel<State, Action, Event>(
    initialState = State(
        theme = getDefaultThemeUseCase(),
        language = getDefaultLanguageUseCase()
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

        getLanguageStateFlowUseCase().onEach { language ->
            setState {
                copy(language = language)
            }
        }.launchIn(viewModelScope)
    }

    override suspend fun reduce(action: Action) = when (action) {
        is Action.ChangeTheme -> changeTheme(action.theme)
        is Action.ChangeLanguage -> changeLanguage(action.language)
        Action.Close -> router.close()
    }

    private suspend fun changeTheme(theme: Theme) {
        changeThemeUseCase(theme)
    }

    private suspend fun changeLanguage(language: Language) {
        changeLanguageUseCase(language)
    }
}