package com.intellect.logos.domain.usecase.settings

import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.repository.SettingsRepository

class ChangeThemeUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(theme: Theme) {
        settingsRepository.changeTheme(theme)
    }
}