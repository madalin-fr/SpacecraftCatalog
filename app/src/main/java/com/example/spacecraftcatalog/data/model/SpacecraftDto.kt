// data/model/SpacecraftDto.kt
package com.example.spacecraftcatalog.data.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class SpacecraftDto(
    val id: Int,
    val name: String,
    @SerializedName("serial_number")
    val serialNumber: String?,
    val description: String?,
    val image: SpacecraftImageDto?, // Add this field
    @SerializedName("spacecraft_config")
    val spacecraftConfig: SpacecraftConfigDto,
    val status: SpacecraftStatusDto
) {
    init {
        Log.d("SpacecraftDto", "Created DTO with agency: ${spacecraftConfig.agency?.id}")
    }
}

data class SpacecraftImageDto(
    @SerializedName("image_url")
    val imageUrl: String?
)


data class SpacecraftStatusDto(
    val id: Int,
    val name: String
)

data class SpacecraftConfigDto(
    val id: Int,
    val url: String,
    val name: String,
    val agency: AgencyDto?
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