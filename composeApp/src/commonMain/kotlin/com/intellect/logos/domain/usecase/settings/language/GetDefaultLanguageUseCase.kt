package com.intellect.logos.domain.usecase.settings.language

import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.repository.SettingsRepository

class GetDefaultLanguageUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Language {
        return settingsRepository.getDefaultLanguage()
    }
}