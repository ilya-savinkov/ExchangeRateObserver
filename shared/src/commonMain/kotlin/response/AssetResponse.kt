package response

import kotlinx.serialization.Serializable

@Serializable
data class AssetResponse(
    val icon: String,
    val currency: CurrencyResponse,
    val country: CountryResponse,
)

@Serializable
data class CurrencyResponse(
    val code: String,
    val description: Map<String, String>,
)

@Serializable
data class CountryResponse(
    val code: String,
    val description: Map<String, String>,
)