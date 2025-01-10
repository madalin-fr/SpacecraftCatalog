package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import javax.inject.Inject

class GetSpacecraftByIdUseCase @Inject constructor(
    private val repository: SpacecraftRepository
) {
    suspend operator fun invoke(id: Int): Spacecraft? {
        return repository.getSpacecraftById(id)
    }
}
