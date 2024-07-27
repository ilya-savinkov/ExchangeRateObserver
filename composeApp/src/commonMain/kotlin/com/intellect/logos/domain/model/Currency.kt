package com.intellect.logos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,
    val description: String
) {
    val isJPY: Boolean = code == "JPY"
}

fun Currency.Companion.empty() = Currency(
    code = "USD",
    description = "United States Dollar",
)