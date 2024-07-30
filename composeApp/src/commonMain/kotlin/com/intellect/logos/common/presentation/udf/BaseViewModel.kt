package com.intellect.logos.common.presentation.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UDF.State, A : UDF.Action, E : UDF.Event>(
    initialState: S,
) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _event: Channel<E> = Channel()
    val event: Flow<E> = _event.receiveAsFlow()

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

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _event.send(event)
        }
    }
}