package response

import kotlinx.serialization.Serializable

@Serializable
data class RateResponse(
    val from: String,
    val to: String,
    val rate: Double,
)