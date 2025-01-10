// ui/viewmodel/SpacecraftListViewModel.kt
package com.example.spacecraftcatalog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacecraftcatalog.domain.usecase.GetAllSpacecraftUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshSpacecraftUseCase
import com.example.spacecraftcatalog.ui.state.SpacecraftListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpacecraftListViewModel @Inject constructor(
    private val getAllSpacecraftUseCase: GetAllSpacecraftUseCase,
    private val refreshSpacecraftUseCase: RefreshSpacecraftUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SpacecraftListState())
    val state: StateFlow<SpacecraftListState> = _state.asStateFlow()

    init {
        getSpacecraft()
        refreshSpacecraft()
    }

    private fun getSpacecraft() {
        getAllSpacecraftUseCase()
            .onEach { spacecraft ->
                _state.update {
                    it.copy(
                        spacecraft = spacecraft,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { error ->
                _state.update {
                    it.copy(
                        error = error.message ?: "Unknown error",
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshSpacecraft(shuffle: Boolean = false) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                refreshSpacecraftUseCase(shuffle)
                getSpacecraft()
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message,
                    isLoading = false
                ) }
            }
        }
    }
}