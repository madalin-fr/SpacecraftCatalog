// data/db/Entities.kt
package com.example.spacecraftcatalog.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agencies")
data class AgencyEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val foundingYear: String?,
    val administrator: String?,
    val url: String?
)

@Entity(tableName = "spacecraft")
data class SpacecraftEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val serialNumber: String?,
    val description: String?,
    val imageUrl: String?,
    val agencyId: Int?,
    val status: String
)