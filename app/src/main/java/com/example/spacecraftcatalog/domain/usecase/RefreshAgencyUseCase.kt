package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import javax.inject.Inject

// domain/usecase/RefreshAgencyUseCase.kt
class RefreshAgencyUseCase @Inject constructor(
    private val repository: AgencyRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.refreshAgency(id)
    }
}