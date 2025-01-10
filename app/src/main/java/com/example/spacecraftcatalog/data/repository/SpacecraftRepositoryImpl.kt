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

            val spacecraftEntities = response.results.map { dto ->
                dto.toSpacecraftEntity(requestedAgencyId = agencyId)
            }

            // Validate before insertion
            val validEntities = spacecraftEntities.filter { it.agencyId != null }
            if (validEntities.isEmpty()) {
                throw IllegalStateException("No valid spacecraft entities created for agency $agencyId")
            }

            dao.upsertSpacecraftForAgency(agencyId, validEntities)
            Log.d("SpacecraftRepository", "Successfully updated ${validEntities.size} spacecraft records")

        } catch (e: Exception) {
            Log.e("SpacecraftRepository", "Failed to refresh spacecraft", e)
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