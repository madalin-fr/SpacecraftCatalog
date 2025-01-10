// domain/usecase/GetSpacecraftForAgencyUseCase.kt
package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpacecraftForAgencyUseCase @Inject constructor(
    private val repository: SpacecraftRepository
) {
    operator fun invoke(agencyId: Int): Flow<List<Spacecraft>> =
        repository.getSpacecraftByAgency(agencyId)
}