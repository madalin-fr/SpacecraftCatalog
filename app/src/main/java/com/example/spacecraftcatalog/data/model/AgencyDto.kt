// data/model/AgencyDto.kt
package com.example.spacecraftcatalog.data.model

import com.google.gson.annotations.SerializedName

data class AgencyDto(
    val id: Int,
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("founding_year")
    val foundingYear: String?,
    val administrator: String?,
    val url: String?
)