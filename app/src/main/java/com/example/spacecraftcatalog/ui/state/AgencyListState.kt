// ui/state/AgencyListState.kt
package com.example.spacecraftcatalog.ui.state

import com.example.spacecraftcatalog.domain.model.Agency

data class AgencyListState(
    val agencies: List<Agency> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)