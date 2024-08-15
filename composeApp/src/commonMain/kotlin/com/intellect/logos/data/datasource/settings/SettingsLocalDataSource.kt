package com.intellect.logos.data.datasource.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.intellect.logos.domain.model.settings.Language
import com.intellect.logos.domain.model.settings.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class SettingsLocalDataSource(
    @Provided private val dataStore: DataStore<Preferences>
) {
    private val themeKey: Preferences.Key<String> = stringPreferencesKey("theme")
    private val languageKey: Preferences.Key<String> = stringPreferencesKey("language")

    fun getThemeStateFlow(): StateFlow<Theme> {
        return dataStore.data.map {
            Theme.valueOfOrDefault(it[themeKey])
        }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, getDefaultTheme())
    }

    fun getDefaultTheme(): Theme {
        return runBlocking {
            Theme.valueOfOrDefault(dataStore.data.first()[themeKey])
        }
    }

    suspend fun changeTheme(theme: Theme) {
        dataStore.edit {
            it[themeKey] = theme.name
        }
    }

    fun getLanguageStateFlow(): StateFlow<Language> {
        return dataStore.data.map {
            Language.valueOfOrDefault(it[languageKey])
        }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, getDefaultLanguage())
    }

    fun getDefaultLanguage(): Language {
        return runBlocking {
            Language.valueOfOrDefault(dataStore.data.first()[languageKey])
        }
    }

    suspend fun changeLanguage(language: Language) {
        dataStore.edit {
            it[languageKey] = language.name
        }
    }
}