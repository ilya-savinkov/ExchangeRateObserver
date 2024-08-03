package com.intellect.logos

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.intellect.logos.domain.model.Theme
import com.intellect.logos.domain.usecase.settings.GetThemeStateFlowUseCase
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class AndroidApp : Application() {

    private val getThemeStateFlowUseCase: GetThemeStateFlowUseCase by inject()

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
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }
}