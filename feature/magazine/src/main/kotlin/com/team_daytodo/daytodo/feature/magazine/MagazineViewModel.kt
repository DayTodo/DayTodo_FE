package com.team_daytodo.daytodo.feature.magazine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.magazine.usecase.GetMagazinePlaceDetailUseCase
import com.team_daytodo.daytodo.domain.magazine.usecase.ToggleMagazinePlaceSaveUseCase
import com.team_daytodo.daytodo.feature.magazine.model.MagazineEvent
import com.team_daytodo.daytodo.feature.magazine.model.MagazineUiState
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
class MagazineViewModel @Inject constructor(
    private val getMagazinePlaceDetailUseCase: GetMagazinePlaceDetailUseCase,
    private val toggleMagazinePlaceSaveUseCase: ToggleMagazinePlaceSaveUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MagazineUiState(isLoading = true))
    val uiState: StateFlow<MagazineUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MagazineEvent>()
    val event: SharedFlow<MagazineEvent> = _event.asSharedFlow()

    private var loadedPlaceId: String? = null

    fun loadPlace(placeId: String) {
        if (loadedPlaceId == placeId && _uiState.value.place != null) return
        loadedPlaceId = placeId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getMagazinePlaceDetailUseCase(placeId)
                .onSuccess { place ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            place = place,
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
                    _event.emit(MagazineEvent.ShowMessage(message))
                }
        }
    }

    fun toggleSaved() {
        val placeId = _uiState.value.place?.id ?: loadedPlaceId ?: return

        viewModelScope.launch {
            toggleMagazinePlaceSaveUseCase(placeId)
                .onSuccess { place ->
                    _uiState.update { it.copy(place = place) }
                    val message = if (place.isSaved) "저장했어요" else "저장을 취소했어요"
                    _event.emit(MagazineEvent.ShowMessage(message))
                }
                .onFailure { cause ->
                    _event.emit(MagazineEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "요청을 처리하지 못했어요."
