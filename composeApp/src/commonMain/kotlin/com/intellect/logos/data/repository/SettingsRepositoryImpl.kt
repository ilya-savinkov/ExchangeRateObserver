package com.intellect.logos.data.repository

import com.intellect.logos.data.datasource.settings.SettingsLocalDataSource
import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {

    override fun getThemeStateFlow(): StateFlow<Theme> {
        return settingsLocalDataSource.getTheme()
    }

    override fun getDefaultTheme(): Theme {
        return settingsLocalDataSource.getDefaultTheme()
    }

    override suspend fun changeTheme(theme: Theme) {
        settingsLocalDataSource.changeTheme(theme)
    }
}