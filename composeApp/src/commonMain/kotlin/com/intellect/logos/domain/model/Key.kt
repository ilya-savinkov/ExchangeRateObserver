package com.intellect.logos.domain.model

sealed class Key {
    data class Number(val value: Int) : Key()
    data object Dot : Key()
    data object Backspace : Key()
}