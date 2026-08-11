package com.team_daytodo.daytodo.feature.magazine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.magazine.usecase.CreateBookmarkUseCase
import com.team_daytodo.daytodo.domain.magazine.usecase.DeleteBookmarkUseCase
import com.team_daytodo.daytodo.domain.magazine.usecase.GetBookmarksUseCase
import com.team_daytodo.daytodo.domain.magazine.usecase.GetMagazineDetailUseCase
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
    private val getMagazineDetailUseCase: GetMagazineDetailUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val createBookmarkUseCase: CreateBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MagazineUiState(isLoading = true))
    val uiState: StateFlow<MagazineUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MagazineEvent>()
    val event: SharedFlow<MagazineEvent> = _event.asSharedFlow()

    private var loadedPlaceId: String? = null

    fun loadPlace(placeId: String) {
        if (loadedPlaceId == placeId && _uiState.value.detail != null) return
        loadedPlaceId = placeId

        val magazineId = placeId.toLongOrNull()
        if (magazineId == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "매거진을 찾을 수 없어요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getMagazineDetailUseCase(magazineId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            detail = detail,
                            errorMessage = null,
                        )
                    }
                    loadBookmarkStatus(magazineId)
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

    // BE에 단건 저장 여부 조회 API가 없어, 저장 목록 전체를 조회해 magazineId가 일치하는 항목으로 판단한다.
    private fun loadBookmarkStatus(magazineId: Long) {
        viewModelScope.launch {
            getBookmarksUseCase(SavedPlaceSortType.RecentSaved)
                .onSuccess { bookmarks ->
                    val matched = bookmarks.find { it.magazineId == magazineId }
                    _uiState.update { it.copy(isSaved = matched != null, bookmarkId = matched?.bookmarkId) }
                }
                .onFailure { /* 저장 여부 확인 실패는 상세 조회 자체를 막지 않는다 */ }
        }
    }

    fun toggleSaved() {
        val magazineId = loadedPlaceId?.toLongOrNull() ?: return
        val state = _uiState.value

        viewModelScope.launch {
            if (state.isSaved) {
                val bookmarkId = state.bookmarkId ?: return@launch
                deleteBookmarkUseCase(bookmarkId)
                    .onSuccess {
                        _uiState.update { it.copy(isSaved = false, bookmarkId = null) }
                        _event.emit(MagazineEvent.ShowMessage("저장을 취소했어요"))
                    }
                    .onFailure { cause -> _event.emit(MagazineEvent.ShowMessage(cause.userFacingMessage())) }
            } else {
                createBookmarkUseCase(magazineId)
                    .onSuccess { bookmarkId ->
                        _uiState.update { it.copy(isSaved = true, bookmarkId = bookmarkId) }
                        _event.emit(MagazineEvent.ShowMessage("저장했어요"))
                    }
                    .onFailure { cause -> _event.emit(MagazineEvent.ShowMessage(cause.userFacingMessage())) }
            }
        }
    }
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "요청을 처리하지 못했어요."
