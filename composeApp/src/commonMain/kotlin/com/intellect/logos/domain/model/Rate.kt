package com.intellect.logos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Rate(
    val assets: Pair<String, String>,
    val volume: Double,
)

fun Rate.Companion.empty() = Rate(
    assets = "" to "",
    volume = 0.0,
)