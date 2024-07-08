package com.intellect.logos.presentation.router

import androidx.navigation.NavController
import com.intellect.logos.domain.model.Asset
import com.intellect.logos.presentation.extension.setResult
import com.intellect.logos.presentation.screen.assets.AssetsRouter
import com.intellect.logos.presentation.screen.assets.AssetsViewModel

class AssetsRouterImpl(private val navController: NavController) : AssetsRouter {

    override fun closeWithResult(asset: Asset) {
        navController.setResult(AssetsViewModel.SELECTED_ASSET_KEY, asset)
        navController.popBackStack()
    }

    override fun close() {
        navController.popBackStack()
    }
}