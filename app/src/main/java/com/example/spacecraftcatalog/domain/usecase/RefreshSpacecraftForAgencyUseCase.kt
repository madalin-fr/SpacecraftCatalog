// domain/usecase/RefreshSpacecraftForAgencyUseCase.kt
package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import javax.inject.Inject

class RefreshSpacecraftForAgencyUseCase @Inject constructor(
    private val repository: SpacecraftRepository
) {
    suspend operator fun invoke(agencyId: Int) =
        repository.refreshSpacecraftForAgency(agencyId)
}