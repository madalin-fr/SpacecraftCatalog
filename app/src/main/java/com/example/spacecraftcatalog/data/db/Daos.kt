// data/db/Daos.kt
package com.example.spacecraftcatalog.data.db

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
    fun getSpacecraftByAgency(agencyId: Int): Flow<List<SpacecraftEntity>>

    @Query("SELECT * FROM spacecraft WHERE id = :id")
    suspend fun getSpacecraftById(id: Int): SpacecraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpacecraft(spacecraft: List<SpacecraftEntity>)

    @Delete
    suspend fun deleteSpacecraft(spacecraft: SpacecraftEntity)
}