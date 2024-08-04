package com.intellect.logos.domain.usecase.settings.language

import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.repository.SettingsRepository

class ChangeLanguageUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(language: Language) {
        settingsRepository.changeLanguage(language)
    }
}