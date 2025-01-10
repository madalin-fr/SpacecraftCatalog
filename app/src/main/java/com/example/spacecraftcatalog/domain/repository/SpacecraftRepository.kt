// domain/repository/SpacecraftRepository.kt
package com.example.spacecraftcatalog.domain.repository

import com.example.spacecraftcatalog.domain.model.Spacecraft
import kotlinx.coroutines.flow.Flow

interface SpacecraftRepository {
    fun getAllSpacecraft(): Flow<List<Spacecraft>>
    suspend fun refreshSpacecraft()
    suspend fun getSpacecraftById(id: Int): Spacecraft?
}