// domain/model/Spacecraft.kt
package com.example.spacecraftcatalog.domain.model

data class Spacecraft(
    val id: Int,
    val name: String,
    val serialNumber: String?,
    val description: String,
    val imageUrl: String?,
    val agency: Agency?,
    val status: SpacecraftStatus
)

enum class SpacecraftStatus {
    ACTIVE,
    RETIRED,
    IN_DEVELOPMENT,
    UNKNOWN
}