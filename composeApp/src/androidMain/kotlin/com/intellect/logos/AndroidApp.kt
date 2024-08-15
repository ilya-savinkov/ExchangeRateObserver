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
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    private val getThemeStateFlowUseCase: GetThemeStateFlowUseCase by inject()
    private val getLanguageStateFlowUseCase: GetLanguageStateFlowUseCase by inject()

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())

        startKoin {
            androidContext(this@AndroidApp)
            androidLogger()

            modules(
                platformModule,
                apiModule,
                dbModule,
                AppModule().module
            )
        }

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