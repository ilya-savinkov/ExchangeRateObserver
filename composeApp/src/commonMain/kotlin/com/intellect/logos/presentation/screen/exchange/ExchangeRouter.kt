package com.intellect.logos.presentation.screen.exchange

import com.intellect.logos.domain.model.Asset

interface ExchangeRouter {
    fun openSettings()
    fun openAssets(assetType: Asset.Type)
}