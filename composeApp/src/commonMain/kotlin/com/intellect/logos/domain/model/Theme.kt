package com.intellect.logos.domain.model

enum class Theme {
    System,
    Dark,
    Light;

    companion object {
        fun valueOfOrDefault(value: String?): Theme {
            return if (value == null) {
                System
            } else {
                try {
                    Theme.valueOf(value)
                } catch (e: IllegalArgumentException) {
                    System
                }
            }
        }
    }
}