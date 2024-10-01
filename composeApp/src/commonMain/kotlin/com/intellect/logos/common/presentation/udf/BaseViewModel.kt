package com.intellect.logos.common.presentation.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UDF.State, A : UDF.Action, E : UDF.Event, M : UDF.Model>(
    protected val model: M
) : ViewModel() {

    private val _event: Channel<E> = Channel()
    val event: Flow<E> = _event.receiveAsFlow()


    fun onAction(action: A) {
        viewModelScope.launch {
            reduce(action)
        }
    }

    abstract suspend fun reduce(action: A)

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _event.send(event)
        }
    }
}