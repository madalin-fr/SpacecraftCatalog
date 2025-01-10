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
    @Query("SELECT * FROM spacecraft WHERE agencyId = :agencyId")
    fun getSpacecraftByAgency(agencyId: Int?): Flow<List<SpacecraftEntity>> //agencyId is nullable

    @Query("SELECT * FROM spacecraft WHERE id = :id")
    suspend fun getSpacecraftById(id: Int): SpacecraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpacecraft(spacecraft: List<SpacecraftEntity>)

    @Query("DELETE FROM spacecraft WHERE agencyId = :agencyId")
    suspend fun deleteSpacecraftByAgency(agencyId: Int)

    @Transaction
    suspend fun upsertSpacecraftForAgency(agencyId: Int, spacecraft: List<SpacecraftEntity>) {
        Log.d("SpacecraftDao", "Deleting spacecraft for agencyId: $agencyId")
        deleteSpacecraftByAgency(agencyId)
        Log.d("SpacecraftDao", "Inserting spacecraft: $spacecraft")
        insertSpacecraft(spacecraft)
    }

    @Delete
    suspend fun deleteSpacecraft(spacecraft: SpacecraftEntity)
}