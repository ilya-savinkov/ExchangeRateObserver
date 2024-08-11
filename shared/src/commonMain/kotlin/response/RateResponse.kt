package response

import kotlinx.serialization.Serializable

@Serializable
data class RateResponse(
    val baseAssetName: String,
    val quoteAssetName: String,
    val rate: Double,
)