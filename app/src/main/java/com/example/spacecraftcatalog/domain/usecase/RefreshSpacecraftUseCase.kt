package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import javax.inject.Inject

class RefreshSpacecraftUseCase @Inject constructor(
    private val repository: SpacecraftRepository
) {
    suspend operator fun invoke(shuffle: Boolean = false) {
        repository.refreshSpacecraft(shuffle)
    }
}