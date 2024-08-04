package com.intellect.logos.domain.repository

import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.model.settings.Theme
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    fun getThemeStateFlow(): StateFlow<Theme>
    fun getDefaultTheme(): Theme
    suspend fun changeTheme(theme: Theme)

    fun getLanguageStateFlow(): StateFlow<Language>
    fun getDefaultLanguage(): Language
    suspend fun changeLanguage(language: Language)
}