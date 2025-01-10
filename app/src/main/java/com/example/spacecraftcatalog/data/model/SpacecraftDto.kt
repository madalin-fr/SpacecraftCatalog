// data/model/SpacecraftDto.kt
package com.example.spacecraftcatalog.data.model

import com.google.gson.annotations.SerializedName

data class SpacecraftDto(
    val id: Int,
    val name: String,
    @SerializedName("serial_number")
    val serialNumber: String?,
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val agency: AgencyDto?,
    val status: SpacecraftStatus
)

enum class SpacecraftStatus {
    @SerializedName("Active")
    ACTIVE,
    @SerializedName("Retired")
    RETIRED,
    @SerializedName("In Development")
    IN_DEVELOPMENT,
    @SerializedName("Unknown")
    UNKNOWN
}