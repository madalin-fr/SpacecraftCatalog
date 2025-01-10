// data/db/SpaceCatalogDatabase.kt
package com.example.spacecraftcatalog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [AgencyEntity::class, SpacecraftEntity::class],
    version = 3,
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
            }
        }

        // New migration for version 2 to 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create the new table without the foreign key
                db.execSQL("""
                    CREATE TABLE spacecraft_new (
                        id INTEGER PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        serialNumber TEXT,
                        description TEXT,
                        imageUrl TEXT,
                        agencyId INTEGER,
                        status TEXT NOT NULL
                    )
                """)

                // Copy the data from the old table to the new table
                db.execSQL("""
                    INSERT INTO spacecraft_new (id, name, serialNumber, description, imageUrl, agencyId, status)
                    SELECT id, name, serialNumber, description, imageUrl, agencyId, status
                    FROM spacecraft
                """)

                // Remove the old table
                db.execSQL("DROP TABLE spacecraft")

                // Rename the new table to the original table name
                db.execSQL("ALTER TABLE spacecraft_new RENAME TO spacecraft")
            }
        }

        fun create(
            context: Context
        ): SpaceCatalogDatabase {
            return Room.databaseBuilder(
                context,
                SpaceCatalogDatabase::class.java,
                "space_catalog_db"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add specific migration
                .fallbackToDestructiveMigration() // Fallback if migration fails
                .build()
        }
    }
}