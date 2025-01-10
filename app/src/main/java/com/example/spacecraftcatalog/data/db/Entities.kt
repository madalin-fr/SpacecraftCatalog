// data/db/Entities.kt
package com.example.spacecraftcatalog.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

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

@Entity(
    tableName = "spacecraft",
    foreignKeys = [
        ForeignKey(
            entity = AgencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["agencyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SpacecraftEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val serialNumber: String?,
    val description: String?,
    val imageUrl: String?,
    val agencyId: Int?,
    val status: String
)