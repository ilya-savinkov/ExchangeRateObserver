package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.presentation.screen.assets.AssetsRouter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory(binds = [AssetsRouter::class])
class AssetsRouterImpl(@Provided private val navController: NavController) : AssetsRouter {

    override fun close() {
        navController.popBackStack()
    }
}