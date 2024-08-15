package com.intellect.logos.common.presentation.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

abstract class BaseViewModel<S : UDF.State, A : UDF.Action, E : UDF.Event>(
    initialState: S,
) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state
        .onStart { onInit() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = initialState
        )

    private val _event: Channel<E> = Channel()
    val event: Flow<E> = _event.receiveAsFlow()

    protected val currentState: S
        get() = state.value

    fun onAction(action: A) {
        viewModelScope.launch {
            reduce(action)
        }
    }

    abstract suspend fun onInit()
    abstract suspend fun reduce(action: A)

    protected fun setState(update: S.() -> S) {
        _state.update {
            it.update()
        }
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _event.send(event)
        }
    }
}