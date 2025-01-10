// data/db/Entities.kt
package com.example.spacecraftcatalog.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

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
            onDelete = ForeignKey.CASCADE,
            // Add this to allow null agencyId values
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("agencyId")]
)
data class SpacecraftEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val serialNumber: String?,
    val description: String?,
    val imageUrl: String?,
    // Keep nullable but add default null
    val agencyId: Int? = null,
    val status: String
)