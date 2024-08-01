package com.intellect.logos.presentation.screen.settings

import com.intellect.logos.common.presentation.udf.UDF

object StateUDF {
    class State : UDF.State

    sealed interface Action : UDF.Action {
        data object Close : Action
    }

    class Event : UDF.Event
}