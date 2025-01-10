package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import javax.inject.Inject

// domain/usecase/GetAgencyByIdUseCase.kt
class GetAgencyByIdUseCase @Inject constructor(
    private val repository: AgencyRepository
) {
    suspend operator fun invoke(id: Int): Agency? {
        return repository.getAgencyById(id)
    }
}