package com.intellect.logos.presentation.extension

import androidx.navigation.NavController
import com.intellect.logos.defaultJson
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

inline fun <reified T> NavController.navigate(
    route: String,
    vararg argument: Pair<String, T>,
) {
    navigate(route)

    currentBackStackEntry?.savedStateHandle?.apply {
        argument.forEach { (key, value) ->
            set(key, defaultJson.encodeToString(value))
        }
    }
}

inline fun <reified T> NavController.setResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, defaultJson.encodeToString(value))
}

suspend inline fun <reified T> NavController.listenToResult(
    key: String,
    crossinline onResult: (T) -> Unit,
) {
    previousBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
        savedStateHandle.getStateFlow<String?>(key, null)
            .filterNotNull()
            .map { defaultJson.decodeFromString<T>(it) }
            .first()
            .let(onResult)

        savedStateHandle.remove<String>(key)
    }
}