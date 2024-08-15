package response

import kotlinx.serialization.Serializable

@Serializable
data class AssetResponse(
    val icon: String,
    val name: String,
    val description: String
)
