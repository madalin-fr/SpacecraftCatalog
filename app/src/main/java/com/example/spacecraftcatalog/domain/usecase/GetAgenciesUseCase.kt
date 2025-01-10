// domain/usecase/GetAgenciesUseCase.kt
package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAgenciesUseCase @Inject constructor(
    private val repository: AgencyRepository
) {
    operator fun invoke(): Flow<List<Agency>> = repository.getAgencies()
}