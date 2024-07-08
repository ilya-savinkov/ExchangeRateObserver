package com.intellect.logos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val code: String,
    val description: Map<String, String>,
)

fun Country.Companion.empty() = Country(
    code = "US",
    description = emptyMap(),
)