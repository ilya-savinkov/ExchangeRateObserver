package com.intellect.logos.domain.model.exchange

import kotlinx.serialization.Serializable

@Serializable
data class Rate(
    val assets: Pair<String, String>,
    val value: Double,
)

fun Rate.Companion.empty() = Rate(
    assets = "" to "",
    value = 0.0,
)