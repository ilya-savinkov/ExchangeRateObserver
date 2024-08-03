package com.intellect.logos.domain.usecase.settings

import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.repository.SettingsRepository

class GetDefaultThemeUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Theme = settingsRepository.getDefaultTheme()
}