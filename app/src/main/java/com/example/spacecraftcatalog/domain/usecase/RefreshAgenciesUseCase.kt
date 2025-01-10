// domain/usecase/RefreshAgenciesUseCase.kt
package com.example.spacecraftcatalog.domain.usecase

import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import javax.inject.Inject

class RefreshAgenciesUseCase @Inject constructor(
    private val repository: AgencyRepository
) {
    suspend operator fun invoke(shuffle: Boolean) {
        repository.refreshAgencies(shuffle)
    }
}