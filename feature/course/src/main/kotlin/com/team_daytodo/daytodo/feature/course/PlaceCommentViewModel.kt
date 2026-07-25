package com.team_daytodo.daytodo.feature.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.course.usecase.AddPlaceCommentUseCase
import com.team_daytodo.daytodo.domain.course.usecase.GetPlaceCommentsUseCase
import com.team_daytodo.daytodo.feature.course.model.PlaceCommentEvent
import com.team_daytodo.daytodo.feature.course.model.PlaceCommentUiState
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
class PlaceCommentViewModel @Inject constructor(
    private val getPlaceCommentsUseCase: GetPlaceCommentsUseCase,
    private val addPlaceCommentUseCase: AddPlaceCommentUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceCommentUiState(isLoading = true))
    val uiState: StateFlow<PlaceCommentUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PlaceCommentEvent>()
    val event: SharedFlow<PlaceCommentEvent> = _event.asSharedFlow()

    private var loadedCourseId: String? = null
    private var loadedPlaceId: String? = null

    fun loadComments(
        courseId: String,
        placeId: String,
    ) {
        if (
            loadedCourseId == courseId &&
            loadedPlaceId == placeId &&
            _uiState.value.place != null
        ) {
            return
        }
        loadedCourseId = courseId
        loadedPlaceId = placeId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getPlaceCommentsUseCase(courseId, placeId)
                .onSuccess { thread ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            place = thread.place,
                            comments = thread.comments,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = cause.userFacingMessage(),
                        )
                    }
                    _event.emit(PlaceCommentEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }

    fun updateInput(input: String) {
        _uiState.update { it.copy(input = input) }
    }

    fun submitComment() {
        val courseId = loadedCourseId ?: return
        val placeId = loadedPlaceId ?: return
        val content = _uiState.value.input
        if (content.isBlank() || _uiState.value.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            addPlaceCommentUseCase(courseId, placeId, content)
                .onSuccess { thread ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            input = "",
                            place = thread.place,
                            comments = thread.comments,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isSubmitting = false) }
                    _event.emit(PlaceCommentEvent.ShowMessage(cause.userFacingMessage()))
                }
        }
    }
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "댓글을 처리하지 못했어요."
