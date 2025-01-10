// data/repository/AgencyRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.AgencyDao
import com.example.spacecraftcatalog.data.mapper.toAgency
import com.example.spacecraftcatalog.data.mapper.toAgencyEntity
import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AgencyRepositoryImpl @Inject constructor(
    private val api: SpaceAgencyApi,
    private val dao: AgencyDao
) : AgencyRepository {

    override fun getAgencies(): Flow<List<Agency>> {
        return dao.getAllAgencies().map { entities ->
            entities.map { it.toAgency() }
        }
    }

    override suspend fun refreshAgencies() {
        try {
            val response = api.getAgencies(limit = 100) // Adjust limit as needed
            dao.insertAgencies(response.results.map { it.toAgencyEntity() })
        } catch (e: Exception) {
            throw e // In real app, you might want to handle this more gracefully
        }
    }

    override suspend fun getAgencyById(id: Int): Agency? {
        return dao.getAgencyById(id)?.toAgency()
    }
}
