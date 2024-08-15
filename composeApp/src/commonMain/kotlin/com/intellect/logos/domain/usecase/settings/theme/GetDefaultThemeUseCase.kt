package com.intellect.logos.domain.usecase.settings.theme

import com.intellect.logos.domain.model.settings.Theme
import com.intellect.logos.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

@Factory
class GetDefaultThemeUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Theme = settingsRepository.getDefaultTheme()
}