package com.intellect.logos.presentation.screen.exchange

import com.intellect.logos.domain.model.Asset

interface ExchangeRouter {
    suspend fun openAssets(selectedAsset: Asset, onResult: (Asset) -> Unit)
}