package com.intellect.logos.presentation.screen.settings

import com.intellect.logos.common.presentation.udf.UDF
import com.intellect.logos.domain.model.Theme

object StateUDF {
    data class State(
        val theme: Theme
    ) : UDF.State

    sealed interface Action : UDF.Action {
        data class ChangeTheme(val theme: Theme) : Action
        data object Close : Action
    }

    class Event : UDF.Event
}