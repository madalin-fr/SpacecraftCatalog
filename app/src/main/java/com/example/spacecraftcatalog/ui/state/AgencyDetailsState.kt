package com.example.spacecraftcatalog.ui.state

import com.example.spacecraftcatalog.domain.model.Agency

// ui/state/AgencyDetailsState.kt
data class AgencyDetailsState(
    val agency: Agency? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)