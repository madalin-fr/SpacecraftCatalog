// ui/viewmodel/SpacecraftListViewModel.kt
package com.example.spacecraftcatalog.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacecraftcatalog.domain.usecase.GetSpacecraftForAgencyUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshSpacecraftForAgencyUseCase
import com.example.spacecraftcatalog.ui.state.SpacecraftListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpacecraftListViewModel @Inject constructor(
    private val getSpacecraftForAgencyUseCase: GetSpacecraftForAgencyUseCase,
    private val refreshSpacecraftForAgencyUseCase: RefreshSpacecraftForAgencyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val agencyId: Int = checkNotNull(savedStateHandle["agencyId"])

    private val _state = MutableStateFlow(SpacecraftListState())
    val state: StateFlow<SpacecraftListState> = _state

    init {
        getSpacecraft()
        refreshSpacecraft()
    }

    private fun getSpacecraft() {
        getSpacecraftForAgencyUseCase(agencyId)
            .onEach { spacecraft ->
                _state.value = _state.value.copy(
                    spacecraft = spacecraft,
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

    fun refreshSpacecraft() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                refreshSpacecraftForAgencyUseCase(agencyId)
                getSpacecraft() // Load after refreshing
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}