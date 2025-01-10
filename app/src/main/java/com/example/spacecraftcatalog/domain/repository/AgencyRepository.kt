// domain/repository/AgencyRepository.kt
package com.example.spacecraftcatalog.domain.repository

import com.example.spacecraftcatalog.domain.model.Agency
import kotlinx.coroutines.flow.Flow

interface AgencyRepository {
    fun getAgencies(): Flow<List<Agency>>
    suspend fun refreshAgencies()
    suspend fun getAgencyById(id: Int): Agency?
}
