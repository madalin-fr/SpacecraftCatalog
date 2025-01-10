// data/repository/SpacecraftRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

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
        return dao.getSpacecraftByAgency(agencyId).map { entities ->
            entities.map { it.toSpacecraft() }
        }
    }

    override suspend fun refreshSpacecraftForAgency(agencyId: Int) {
        try {
            val response = api.getSpacecraft(limit = 100, agencyId = agencyId)
            dao.insertSpacecraft(response.results.map { it.toSpacecraftEntity() })
        } catch (e: Exception) {
            throw e // In real app, you might want to handle this more gracefully
        }
    }

    override suspend fun getSpacecraftById(id: Int): Spacecraft? {
        return dao.getSpacecraftById(id)?.toSpacecraft()
    }
}