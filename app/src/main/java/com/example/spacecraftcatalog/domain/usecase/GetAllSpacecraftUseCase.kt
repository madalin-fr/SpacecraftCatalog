package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// domain/usecase/GetAllSpacecraftUseCase.kt
class GetAllSpacecraftUseCase @Inject constructor(
    private val repository: SpacecraftRepository
) {
    operator fun invoke(): Flow<List<Spacecraft>> = repository.getAllSpacecraft()
}

