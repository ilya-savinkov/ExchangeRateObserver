package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.presentation.screen.settings.SettingsRouter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory(binds = [SettingsRouter::class])
class SettingsRouterImpl(@Provided private val navController: NavController) : SettingsRouter {

    override fun close() {
        navController.popBackStack()
    }
}