package com.example.spacecraftcatalog.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacecraftcatalog.domain.usecase.GetAgencyByIdUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshAgencyUseCase
import com.example.spacecraftcatalog.ui.state.AgencyDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ui/viewmodel/AgencyDetailsViewModel.kt
@HiltViewModel
class AgencyDetailsViewModel @Inject constructor(
    private val getAgencyByIdUseCase: GetAgencyByIdUseCase,
    private val refreshAgencyUseCase: RefreshAgencyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val agencyId: Int = checkNotNull(savedStateHandle["agencyId"])

    private val _state = MutableStateFlow(AgencyDetailsState())
    val state: StateFlow<AgencyDetailsState> = _state.asStateFlow()

    init {
        getAgency()
    }

    private fun getAgency() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val agency = getAgencyByIdUseCase(agencyId)
                _state.update { it.copy(
                    agency = agency,
                    isLoading = false,
                    error = null
                ) }
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                ) }
            }
        }
    }

    fun refreshAgency() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                refreshAgencyUseCase(agencyId)
                getAgency()
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                ) }
            }
        }
    }
}