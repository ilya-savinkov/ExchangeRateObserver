package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.Screen
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.screen.exchange.ExchangeRouter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory(binds = [ExchangeRouter::class])
class ExchangeRouterImpl(@Provided private val navController: NavController) : ExchangeRouter {

    override fun openSettings() {
        navController.navigate(Screen.Settings)
    }

    override fun openAssets(assetType: Asset.Type) {
        navController.navigate(
            Screen.Assets(
                assetType = assetType
            )
        )
    }
}