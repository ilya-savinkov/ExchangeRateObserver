package com.intellect.logos

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.intellect.logos.domain.model.settings.Theme
import com.intellect.logos.domain.usecase.settings.language.GetLanguageStateFlowUseCase
import com.intellect.logos.domain.usecase.settings.theme.GetThemeStateFlowUseCase
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class AndroidApp : Application() {

    private val getThemeStateFlowUseCase: GetThemeStateFlowUseCase by inject()
    private val getLanguageStateFlowUseCase: GetLanguageStateFlowUseCase by inject()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
        Napier.base(DebugAntilog())

        getThemeStateFlowUseCase().onEach { theme ->
            AppCompatDelegate.setDefaultNightMode(
                when (theme) {
                    Theme.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    Theme.Dark -> AppCompatDelegate.MODE_NIGHT_YES
                    Theme.Light -> AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }.launchIn(scope)

        getLanguageStateFlowUseCase().onEach { language ->
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.code))
        }.launchIn(scope)
    }
}