// ui/state/SpacecraftDetailsState.kt
package com.example.spacecraftcatalog.ui.state

import com.example.spacecraftcatalog.domain.model.Spacecraft

data class SpacecraftDetailsState(
    val spacecraft: Spacecraft? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)