package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.extension.listenToResult
import com.intellect.logos.presentation.extension.navigate
import com.intellect.logos.presentation.screen.assets.AssetsViewModel
import com.intellect.logos.presentation.screen.exchange.ExchangeRouter

class ExchangeRouterImpl(private val navController: NavController) : ExchangeRouter {

    override suspend fun openAssets(selectedAsset: Asset, onResult: (Asset) -> Unit) {
        navController.navigate(
            route = AssetsViewModel.ROUTE,
            AssetsViewModel.SELECTED_ASSET_KEY to selectedAsset
        )

        navController.listenToResult<Asset>(AssetsViewModel.SELECTED_ASSET_KEY) {
            onResult(it)
        }
    }
}