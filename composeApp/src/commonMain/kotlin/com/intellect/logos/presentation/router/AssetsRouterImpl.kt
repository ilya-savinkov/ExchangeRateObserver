package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.presentation.screen.assets.AssetsRouter

class AssetsRouterImpl(private val navController: NavController) : AssetsRouter {

    override fun close() {
        navController.popBackStack()
    }
}