package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.common.presentation.navigation.navigate
import com.intellect.logos.defaultJson
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.screen.assets.AssetsViewModel
import com.intellect.logos.presentation.screen.exchange.ExchangeRouter
import com.intellect.logos.presentation.screen.settings.SettingsViewModel
import kotlinx.serialization.encodeToString

class ExchangeRouterImpl(private val navController: NavController) : ExchangeRouter {

    override fun openSettings() {
        navController.navigate(SettingsViewModel.ROUTE)
    }

    override fun openAssets(selectedAsset: Asset, type: Asset.Type) {
        navController.navigate(
            route = AssetsViewModel.ROUTE,
            AssetsViewModel.SELECTED_ASSET_KEY to defaultJson.encodeToString(selectedAsset),
            AssetsViewModel.ASSET_TYPE_KEY to type.name
        )
    }
}