package com.intellect.logos.presentation.screen.settings

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.model.settings.Theme

object StateUDF {
    data class State(
        val theme: Theme,
        val language: Language
    ) : UDF.State

    sealed interface Action : UDF.Action {
        data class ChangeTheme(val theme: Theme) : Action
        data class ChangeLanguage(val language: Language) : Action
        data object Close : Action
    }

    class Event : UDF.Event
}