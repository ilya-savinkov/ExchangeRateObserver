package com.intellect.logos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Asset(
    val name: String,
    val description: String,
    val icon: String
) {
    val isJPY: Boolean = name == "JPY"

    enum class Type {
        Base,
        Quote
    }
}

fun Asset.Companion.empty() = Asset(
    name = "",
    description = "",
    icon = ""
)