package com.intellect.logos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Rate(
    val baseAsset: Asset,
    val quoteAsset: Asset,
)

fun Rate.Companion.empty() = Rate(
    baseAsset = Asset.empty(),
    quoteAsset = Asset.empty(),
)