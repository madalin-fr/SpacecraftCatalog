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
    val status: SpacecraftStatusDto
)

data class SpacecraftStatusDto(
    val id: Int,
    val name: String
)

enum class SpacecraftStatus {
    ACTIVE,
    RETIRED,
    IN_DEVELOPMENT,
    UNKNOWN;

    companion object {
        fun fromApiName(name: String): SpacecraftStatus = when (name.uppercase()) {
            "ACTIVE" -> ACTIVE
            "RETIRED" -> RETIRED
            "IN DEVELOPMENT" -> IN_DEVELOPMENT
            "SINGLE USE" -> RETIRED  // Map single use to retired
            else -> UNKNOWN
        }
    }
}