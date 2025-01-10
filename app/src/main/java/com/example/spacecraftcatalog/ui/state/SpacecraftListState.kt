// ui/state/SpacecraftListState.kt
package com.example.spacecraftcatalog.ui.state

import com.example.spacecraftcatalog.domain.model.Spacecraft

data class SpacecraftListState(
    val spacecraft: List<Spacecraft> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)