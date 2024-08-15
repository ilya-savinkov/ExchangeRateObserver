package com.intellect.logos.domain.usecase.settings.language

import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

@Factory
class GetLanguageStateFlowUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): StateFlow<Language> {
        return settingsRepository.getLanguageStateFlow()
    }
}