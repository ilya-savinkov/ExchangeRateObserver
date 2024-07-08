package com.intellect.logos.presentation.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UDF.State, A : UDF.Action, E : UDF.Event>(
    initialState: S,
) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected val currentState: S
        get() = _state.value

    fun onAction(action: A) {
        viewModelScope.launch {
            reduce(action)
        }
    }

    abstract suspend fun reduce(action: A)

    protected fun setState(action: S.() -> S) {
        _state.update {
            it.action()
        }
    }
}