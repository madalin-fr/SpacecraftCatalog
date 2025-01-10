import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacecraftcatalog.domain.usecase.GetSpacecraftByIdUseCase
import com.example.spacecraftcatalog.domain.usecase.RefreshSpacecraftUseCase
import com.example.spacecraftcatalog.ui.state.SpacecraftDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ui/viewmodel/SpacecraftDetailsViewModel.kt
@HiltViewModel
class SpacecraftDetailsViewModel @Inject constructor(
    private val getSpacecraftByIdUseCase: GetSpacecraftByIdUseCase,
    private val refreshSpacecraftUseCase: RefreshSpacecraftUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val spacecraftId: Int = checkNotNull(savedStateHandle["spacecraftId"])

    private val _state = MutableStateFlow(SpacecraftDetailsState())
    val state: StateFlow<SpacecraftDetailsState> = _state

    init {
        getSpacecraft()
        refreshSpacecraft()
    }

    private fun getSpacecraft() {
        viewModelScope.launch {
            try {
                val spacecraft = getSpacecraftByIdUseCase(spacecraftId)
                _state.value = _state.value.copy(
                    spacecraft = spacecraft,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun refreshSpacecraft() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                refreshSpacecraftUseCase(spacecraftId)
                // The spacecraft will be automatically updated through Room's Flow
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}