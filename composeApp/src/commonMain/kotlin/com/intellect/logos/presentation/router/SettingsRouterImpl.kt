package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.presentation.screen.settings.SettingsRouter

class SettingsRouterImpl(private val navController: NavController) : SettingsRouter {

    override fun close() {
        navController.popBackStack()
    }
}