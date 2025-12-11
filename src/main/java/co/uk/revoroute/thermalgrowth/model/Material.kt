package co.uk.revoroute.thermalgrowth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Material(
    @SerialName("name") val name: String,
    @SerialName("alpha") val alpha: Double,
    @SerialName("category") val category: String,
)