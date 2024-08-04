package com.intellect.logos.domain.model.settings

enum class Language(
    val code: String,
    val description: String
) {
    English(
        code = "en",
        description = "English"
    ),
    Russian(
        code = "ru",
        description = "Русский"
    );

    companion object {
        fun valueOfOrDefault(value: String?): Language {
            return if (value == null) {
                English
            } else {
                try {
                    Language.valueOf(value)
                } catch (e: IllegalArgumentException) {
                    English
                }
            }
        }
    }
}