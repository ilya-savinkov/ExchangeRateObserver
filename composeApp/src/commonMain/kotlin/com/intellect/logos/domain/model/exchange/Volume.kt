package com.intellect.logos.domain.model.exchange

import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    val value: Double,
    val text: String
)

fun Volume.Companion.default() = Volume(
    value = 1.0,
    text = "1"
)