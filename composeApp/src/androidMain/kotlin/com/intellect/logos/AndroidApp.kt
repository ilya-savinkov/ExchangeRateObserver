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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration
import org.koin.ksp.generated.module

@OptIn(KoinExperimentalAPI::class)
class AndroidApp : Application(), KoinStartup {

    private val getThemeStateFlowUseCase: GetThemeStateFlowUseCase by inject()
    private val getLanguageStateFlowUseCase: GetLanguageStateFlowUseCase by inject()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        // TODO Send all errors to analytics
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

    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        androidContext(this@AndroidApp)
        androidLogger()

        modules(
            platformModule,
            apiModule,
            dbModule,
            AppModule().module
        )
    }
}