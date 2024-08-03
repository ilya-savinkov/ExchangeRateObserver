package com.intellect.logos.domain.usecase.settings

import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class GetThemeStateFlowUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): StateFlow<Theme> = settingsRepository.getThemeStateFlow()
}