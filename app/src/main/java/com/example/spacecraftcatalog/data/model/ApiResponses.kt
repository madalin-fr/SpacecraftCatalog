// data/model/ApiResponses.kt
package com.example.spacecraftcatalog.data.model

data class AgencyResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<AgencyDto>
)

data class SpacecraftResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<SpacecraftDto>
)