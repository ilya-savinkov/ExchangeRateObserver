package com.intellect.logos.common.presentation.navigation

import androidx.lifecycle.SavedStateHandle
import com.intellect.logos.defaultJson

inline fun <reified T> SavedStateHandle.getObject(key: String): T {
    return defaultJson.decodeFromString(requireNotNull(this[key]))
}

inline fun SavedStateHandle.getString(key: String): String {
    return requireNotNull(this[key])
}