package com.intellect.logos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Asset(
    val icon: String,
    val currency: Currency,
    val country: Country,
)

fun Asset.Companion.empty() = Asset(
    icon = "",
    currency = Currency.empty(),
    country = Country.empty(),
)