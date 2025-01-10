// domain/repository/SpacecraftRepository.kt
package com.example.spacecraftcatalog.domain.repository

import com.example.spacecraftcatalog.domain.model.Spacecraft
import kotlinx.coroutines.flow.Flow

interface SpacecraftRepository {
    fun getSpacecraftByAgency(agencyId: Int): Flow<List<Spacecraft>>
    suspend fun refreshSpacecraftForAgency(agencyId: Int)
    suspend fun getSpacecraftById(id: Int): Spacecraft?
}
