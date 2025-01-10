// ui/viewmodel/AgencyListViewModel.kt
package com.example.spacecraftcatalog.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacecraftcatalog.domain.usecase.GetAgenciesUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshAgenciesUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshSpacecraftForAgencyUseCase
import com.example.spacecraftcatalog.ui.state.AgencyListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgencyListViewModel @Inject constructor(
    private val getAgenciesUseCase: GetAgenciesUseCase,
    private val refreshAgenciesUseCase: RefreshAgenciesUseCase,
    private val refreshSpacecraftForAgencyUseCase: RefreshSpacecraftForAgencyUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AgencyListState())
    val state: StateFlow<AgencyListState> = _state

    init {
        getAgencies()
        refreshAgencies()
    }

    private fun getAgencies() {
        getAgenciesUseCase()
            .onEach { agencies ->
                _state.value = _state.value.copy(
                    agencies = agencies,
                    isLoading = false
                )
            }
            .catch { error ->
                _state.value = _state.value.copy(
                    error = error.message,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun refreshAgencies() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                refreshAgenciesUseCase()
                // No need to call getAgencies() as the Flow will emit new values
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun selectAgency(agencyId: Int) {
        viewModelScope.launch {
            try {
                refreshSpacecraftForAgencyUseCase(agencyId)
            } catch (e: Exception) {
                // Log error but don't update state as this is a background operation
                Log.e("AgencyListViewModel", "Error fetching spacecraft for agency $agencyId", e)
            }
        }
    }
}