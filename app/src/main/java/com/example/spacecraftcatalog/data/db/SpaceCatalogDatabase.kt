// data/db/SpaceCatalogDatabase.kt
package com.example.spacecraftcatalog.data.db

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [AgencyEntity::class, SpacecraftEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SpaceCatalogDatabase : RoomDatabase() {
    abstract fun agencyDao(): AgencyDao
    abstract fun spacecraftDao(): SpacecraftDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // If you need to preserve data, add migration logic here
                // For example:
                // db.execSQL("ALTER TABLE spacecraft ADD COLUMN new_column TEXT")
            }
        }

        fun create(
            context: Context,
            modifier: Modifier = Modifier
        ): SpaceCatalogDatabase {
            return Room.databaseBuilder(
                context,
                SpaceCatalogDatabase::class.java,
                "space_catalog_db"
            )
                .addMigrations(MIGRATION_1_2) // Add specific migration
                .fallbackToDestructiveMigration() // Fallback if migration fails
                .build()
        }
    }
}