package com.intellect.logos.presentation.extension

import androidx.lifecycle.SavedStateHandle
import com.intellect.logos.defaultJson

inline fun <reified T> SavedStateHandle.getValue(key: String): T {
    return defaultJson.decodeFromString(requireNotNull(this[key]))
}