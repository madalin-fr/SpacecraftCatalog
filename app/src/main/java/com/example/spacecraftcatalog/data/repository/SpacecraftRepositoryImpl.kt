// data/repository/SpacecraftRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

import android.util.Log
import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.SpacecraftDao
import com.example.spacecraftcatalog.data.mapper.toSpacecraft
import com.example.spacecraftcatalog.data.mapper.toSpacecraftEntity
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpacecraftRepositoryImpl @Inject constructor(
    private val api: SpaceAgencyApi,
    private val dao: SpacecraftDao
) : SpacecraftRepository {

    override fun getSpacecraftByAgency(agencyId: Int): Flow<List<Spacecraft>> {
        Log.d("SpacecraftRepository", "Fetching spacecraft for agencyId: $agencyId")
        return dao.getSpacecraftByAgency(agencyId).map { entities ->
            Log.d("SpacecraftRepository", "Retrieved ${entities.size} spacecraft(s) from database.")
            if (entities.isEmpty()) {
                Log.w("SpacecraftRepository", "No spacecraft found for agencyId: $agencyId")
            }
            entities.map { it.toSpacecraft() }
        }
    }

    override suspend fun refreshSpacecraftForAgency(agencyId: Int) {
        try {
            Log.d("SpacecraftRepository", "Refreshing spacecraft for agencyId: $agencyId")
            val response = api.getSpacecraft(limit = 100, agencyId = agencyId)
            Log.d("SpacecraftRepository", "API Response: $response")
            if (response.results.isNotEmpty()) {
                val spacecraftEntities = response.results.map { spacecraftDto ->
                    Log.d("SpacecraftRepository", "Mapping SpacecraftDto: $spacecraftDto")
                    // Map agencyId to null if not present in DTO
                    val entity = spacecraftDto.toSpacecraftEntity()
                    Log.d("SpacecraftRepository", "Mapped Entity: $entity")
                    entity
                }
                dao.upsertSpacecraftForAgency(agencyId, spacecraftEntities)
                Log.d("SpacecraftRepository", "Inserted ${spacecraftEntities.size} spacecraft records into the database.")
            } else {
                Log.w("SpacecraftRepository", "No spacecraft data found from API for agencyId: $agencyId")
            }
        } catch (e: Exception) {
            Log.e("SpacecraftRepository", "Failed to refresh spacecraft for agencyId: $agencyId", e)
            throw e
        }
    }

    override suspend fun getSpacecraftById(id: Int): Spacecraft? {
        Log.d("SpacecraftRepository", "Fetching spacecraft by ID: $id")
        return dao.getSpacecraftById(id)?.toSpacecraft().also {
            if (it == null) {
                Log.w("SpacecraftRepository", "No spacecraft found with ID: $id")
            }
        }
    }
}