// ui/viewmodel/AgencyListViewModel.kt
package com.example.spacecraftcatalog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacecraftcatalog.domain.usecase.GetAgenciesUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshAgenciesUseCase
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

    fun refreshAgencies(shuffle: Boolean = false) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                refreshAgenciesUseCase(shuffle) // Pass the shuffle flag
                getAgencies() // Update UI with the latest data
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

}