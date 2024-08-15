package com.intellect.logos.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponse(
    val base: String,
    val rates: Map<String, Double>
)