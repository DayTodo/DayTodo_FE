package com.team_daytodo.daytodo.feature.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.course.usecase.ImportSavedPlacesToCourseUseCase
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.magazine.usecase.GetSavedMagazinePlacesUseCase
import com.team_daytodo.daytodo.feature.save.model.SavedPlacePickerEvent
import com.team_daytodo.daytodo.feature.save.model.SavedPlacePickerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SavedPlacePickerViewModel @Inject constructor(
    private val getSavedMagazinePlacesUseCase: GetSavedMagazinePlacesUseCase,
    private val importSavedPlacesToCourseUseCase: ImportSavedPlacesToCourseUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedPlacePickerUiState(isLoading = true))
    val uiState: StateFlow<SavedPlacePickerUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SavedPlacePickerEvent>()
    val event: SharedFlow<SavedPlacePickerEvent> = _event.asSharedFlow()

    init {
        loadSavedPlaces()
    }

    fun loadSavedPlaces() {
        val sortType = _uiState.value.sortType
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getSavedMagazinePlacesUseCase(sortType)
                .onSuccess { places ->
                    val visiblePlaceIds = places.mapTo(mutableSetOf()) { it.id }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            places = places,
                            selectedPlaceIds = it.selectedPlaceIds intersect visiblePlaceIds,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { cause ->
                    val message = cause.userFacingMessage()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message,
                        )
                    }
                    _event.emit(SavedPlacePickerEvent.ShowMessage(message))
                }
        }
    }

    fun showSortDialog() {
        _uiState.update { it.copy(isSortDialogVisible = true) }
    }

    fun dismissSortDialog() {
        _uiState.update { it.copy(isSortDialogVisible = false) }
    }

    fun selectSortType(sortType: SavedPlaceSortType) {
        _uiState.update {
            it.copy(
                sortType = sortType,
                isSortDialogVisible = false,
            )
        }
        loadSavedPlaces()
    }

    fun togglePlaceSelection(placeId: String) {
        val place = _uiState.value.places.firstOrNull { it.id == placeId }
        if (place?.serverPlaceId == null) {
            viewModelScope.launch {
                _event.emit(SavedPlacePickerEvent.ShowMessage("이 장소는 아직 코스로 불러올 수 없어요."))
            }
            return
        }

        _uiState.update { state ->
            val selectedPlaceIds = state.selectedPlaceIds.toMutableSet()
            if (!selectedPlaceIds.add(placeId)) {
                selectedPlaceIds.remove(placeId)
            }
            state.copy(selectedPlaceIds = selectedPlaceIds)
        }
    }

    fun importSelectedPlaces(courseId: String) {
        val state = _uiState.value
        val selectedPlaceIds = state.selectedServerPlaceIds
        if (state.isImporting) return
        if (selectedPlaceIds.isEmpty()) {
            viewModelScope.launch {
                _event.emit(SavedPlacePickerEvent.ShowMessage("불러올 수 있는 장소를 선택해 주세요."))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isImporting = true) }
            importSavedPlacesToCourseUseCase(
                courseId = courseId,
                placeIds = selectedPlaceIds,
            ).onSuccess {
                _event.emit(SavedPlacePickerEvent.Imported)
            }.onFailure { cause ->
                _event.emit(SavedPlacePickerEvent.ShowMessage(cause.userFacingMessage()))
            }
            _uiState.update { it.copy(isImporting = false) }
        }
    }
}
