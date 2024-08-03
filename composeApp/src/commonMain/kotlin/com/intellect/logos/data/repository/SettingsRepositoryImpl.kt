package com.intellect.logos.data.repository

import com.intellect.logos.data.datasource.settings.SettingsLocalDataSource
import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.model.settings.Theme
import com.intellect.logos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {

    override fun getThemeStateFlow(): StateFlow<Theme> {
        return settingsLocalDataSource.getThemeStateFlow()
    }

    override fun getDefaultTheme(): Theme {
        return settingsLocalDataSource.getDefaultTheme()
    }

    override suspend fun changeTheme(theme: Theme) {
        settingsLocalDataSource.changeTheme(theme)
    }

    override fun getLanguageStateFlow(): StateFlow<Language> {
        return settingsLocalDataSource.getLanguageStateFlow()
    }

    override fun getDefaultLanguage(): Language {
        return settingsLocalDataSource.getDefaultLanguage()
    }

    override suspend fun changeLanguage(language: Language) {
        settingsLocalDataSource.changeLanguage(language)
    }
}