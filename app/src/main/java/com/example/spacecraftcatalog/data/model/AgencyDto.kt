// data/model/AgencyDto.kt
package com.example.spacecraftcatalog.data.model

import com.google.gson.annotations.SerializedName

data class AgencyDto(
    val id: Int,
    val name: String,
    @SerializedName("description")
    val description: String?,
    val image: AgencyImageDto?,  // Changed to match API structure
    @SerializedName("founding_year")
    val foundingYear: String?,
    val administrator: String?,
    val url: String?
)

data class AgencyImageDto(
    @SerializedName("image_url")
    val imageUrl: String?
)