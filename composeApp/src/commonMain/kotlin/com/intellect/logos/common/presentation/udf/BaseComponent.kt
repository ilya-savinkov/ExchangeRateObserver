package com.intellect.logos.common.presentation.udf

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseComponent<S : UDF.State, M : UDF.Model, A : UDF.Action, E : UDF.Event>(
    coroutineScope: CoroutineScope
) : CoroutineScope by coroutineScope {

    private val _event: Channel<E> = Channel()
    val event: Flow<E> = _event.receiveAsFlow()

    abstract val state: S

    fun onAction(action: A) {
        launch {
            reduce(action)
        }
    }

    open suspend fun onInit() {}
    abstract suspend fun reduce(action: A)

    protected fun sendEvent(event: E) {
        launch {
            _event.send(event)
        }
    }

    fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> {
        return stateIn(
            scope = this@BaseComponent,
            started = SharingStarted.Lazily,
            initialValue = initialValue
        )
    }
}