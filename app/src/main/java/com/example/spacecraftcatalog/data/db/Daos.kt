// data/db/Daos.kt
package com.example.spacecraftcatalog.data.db

import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AgencyDao {
    @Query("SELECT * FROM agencies")
    fun getAllAgencies(): Flow<List<AgencyEntity>>

    @Query("SELECT * FROM agencies WHERE id = :id")
    suspend fun getAgencyById(id: Int): AgencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgencies(agencies: List<AgencyEntity>)

    @Delete
    suspend fun deleteAgency(agency: AgencyEntity)
}

@Dao
interface SpacecraftDao {
    @Query("""
        SELECT * FROM spacecraft 
        WHERE agencyId = :agencyId
    """)
    fun getSpacecraftByAgency(agencyId: Int): Flow<List<SpacecraftEntity>>

    @Query("SELECT * FROM spacecraft WHERE id = :id")
    suspend fun getSpacecraftById(id: Int): SpacecraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpacecraft(spacecraft: List<SpacecraftEntity>)

    @Query("DELETE FROM spacecraft WHERE agencyId = :agencyId")
    suspend fun deleteSpacecraftByAgency(agencyId: Int)

    @Transaction
    suspend fun upsertSpacecraftForAgency(agencyId: Int, spacecraft: List<SpacecraftEntity>) {
        Log.d("SpacecraftDao", "Starting upsert for ${spacecraft.size} spacecraft")

        try {
            // First, verify the agency exists
            val validAgencyIds = getValidAgencyIds()

            // Filter spacecraft to only include those with valid agency IDs
            val validSpacecraft = spacecraft.filter { entity ->
                entity.agencyId?.let { id ->
                    validAgencyIds.contains(id)
                } ?: false // Remove entries with null agencyId
            }

            if (validSpacecraft.isEmpty()) {
                Log.w("SpacecraftDao", "No valid spacecraft to insert after filtering")
                return
            }

            // Proceed with deletion and insertion
            deleteSpacecraftByAgency(agencyId)
            insertSpacecraft(validSpacecraft)

            Log.d("SpacecraftDao", "Successfully upserted ${validSpacecraft.size} spacecraft")
        } catch (e: Exception) {
            Log.e("SpacecraftDao", "Error during upsert", e)
            throw e
        }
    }

    @Query("SELECT id FROM agencies")
    suspend fun getValidAgencyIds(): List<Int>

}