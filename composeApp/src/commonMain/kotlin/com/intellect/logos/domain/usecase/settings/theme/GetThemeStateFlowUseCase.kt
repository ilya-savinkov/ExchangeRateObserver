package com.intellect.logos.domain.usecase.settings.theme

import com.intellect.logos.domain.model.settings.Theme
import com.intellect.logos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Factory

@Factory
class GetThemeStateFlowUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): StateFlow<Theme> = settingsRepository.getThemeStateFlow()
}