// data/db/SpaceCatalogDatabase.kt
package com.example.spacecraftcatalog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AgencyEntity::class, SpacecraftEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SpaceCatalogDatabase : RoomDatabase() {
    abstract fun agencyDao(): AgencyDao
    abstract fun spacecraftDao(): SpacecraftDao
}