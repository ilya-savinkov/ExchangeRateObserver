package com.intellect.logos.presentation.screen.assets

import com.intellect.logos.domain.model.Asset

interface AssetsRouter {
    fun closeWithResult(asset: Asset)
    fun close()
}