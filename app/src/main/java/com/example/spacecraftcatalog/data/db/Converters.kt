// data/db/Converters.kt
package com.example.spacecraftcatalog.data.db

import androidx.room.TypeConverter
import com.example.spacecraftcatalog.domain.model.SpacecraftStatus

class Converters {
    @TypeConverter
    fun fromSpacecraftStatus(status: SpacecraftStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSpacecraftStatus(status: String): SpacecraftStatus {
        return try {
            SpacecraftStatus.valueOf(status)
        } catch (e: IllegalArgumentException) {
            SpacecraftStatus.UNKNOWN
        }
    }
}