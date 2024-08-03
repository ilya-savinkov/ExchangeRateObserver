package com.intellect.logos.domain.repository

import com.intellect.logos.domain.model.Theme
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    fun getThemeStateFlow(): StateFlow<Theme>
    fun getDefaultTheme(): Theme
    suspend fun changeTheme(theme: Theme)
}