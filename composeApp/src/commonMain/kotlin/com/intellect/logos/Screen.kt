package com.intellect.logos

import com.intellect.logos.domain.model.Asset
import kotlinx.serialization.Serializable

interface Screen {
    @Serializable
    data object Exchange

    @Serializable
    data class Assets(val assetType: Asset.Type)

    @Serializable
    data object Settings
}