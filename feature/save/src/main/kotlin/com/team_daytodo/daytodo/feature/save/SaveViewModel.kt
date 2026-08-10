package com.team_daytodo.daytodo.feature.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.bookmark.usecase.GetBookmarksUseCase
import com.team_daytodo.daytodo.domain.region.usecase.GetRegionsUseCase
import com.team_daytodo.daytodo.feature.save.model.SaveEvent
import com.team_daytodo.daytodo.feature.save.model.SaveUiState
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
class SaveViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val getRegionsUseCase: GetRegionsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SaveUiState(isLoading = true))
    val uiState: StateFlow<SaveUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SaveEvent>()
    val event: SharedFlow<SaveEvent> = _event.asSharedFlow()

    init {
        loadRegions()
        loadSavedPlaces()
    }

    fun loadSavedPlaces() {
        val sortType = _uiState.value.sortType
        val regionId = _uiState.value.selectedRegionId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getBookmarksUseCase(sortType, regionId)
                .onSuccess { places ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            places = places,
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
                    _event.emit(SaveEvent.ShowMessage(message))
                }
        }
    }

    private fun loadRegions() {
        viewModelScope.launch {
            getRegionsUseCase()
                .onSuccess { regions -> _uiState.update { it.copy(regions = regions) } }
                .onFailure { /* 지역 목록 조회 실패는 필터 없이 저장 목록을 보여주는 것을 막지 않는다 */ }
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

    fun showRegionDialog() {
        _uiState.update { it.copy(isRegionDialogVisible = true) }
    }

    fun dismissRegionDialog() {
        _uiState.update { it.copy(isRegionDialogVisible = false) }
    }

    fun selectRegion(regionId: Long?) {
        _uiState.update {
            it.copy(
                selectedRegionId = regionId,
                isRegionDialogVisible = false,
            )
        }
        loadSavedPlaces()
    }
}

internal fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "요청을 처리하지 못했어요."
