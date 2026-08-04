package com.team_daytodo.daytodo.feature.today.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.today.usecase.CompleteCourseUseCase
import com.team_daytodo.daytodo.domain.today.usecase.GetTodayCourseUseCase
import com.team_daytodo.daytodo.domain.today.usecase.SaveMemoryPhotosUseCase
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace
import com.team_daytodo.daytodo.feature.today.model.TodayEvent
import com.team_daytodo.daytodo.feature.today.state.TodayUiState
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
class TodayViewModel @Inject constructor(
    private val getTodayCourseUseCase: GetTodayCourseUseCase,
    private val completeCourseUseCase: CompleteCourseUseCase,
    private val saveMemoryPhotosUseCase: SaveMemoryPhotosUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TodayEvent>()
    val event: SharedFlow<TodayEvent> = _event.asSharedFlow()

    init {
        loadTodayCourse()
    }

    fun completeCourse() {
        val courseId = _uiState.value.courseId ?: return

        viewModelScope.launch {
            completeCourseUseCase(courseId)
                .onSuccess { loadTodayCourse() }
                .onFailure { cause -> _uiState.update { it.copy(errorMessage = cause.message) } }
        }
    }

    fun selectMemoryPhotos(uris: List<String>) {
        _uiState.update { it.copy(selectedMemoryPhotoUris = uris) }
    }

    // TODO: BE에 이미지 업로드 API가 아직 없어 로컬 갤러리 Uri 문자열을 그대로 imageUrls로 전달한다.
    // 서버에 실제로 접근 가능한 URL이 아니므로, 업로드 API가 생기면 업로드 후 받은 URL로 교체해야 한다.
    fun saveMemoryPhotos(imageUrls: List<String>) {
        val courseId = _uiState.value.courseId ?: return
        if (imageUrls.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingMemoryPhotos = true) }
            saveMemoryPhotosUseCase(courseId, imageUrls)
                .onSuccess {
                    _uiState.update {
                        it.copy(isSavingMemoryPhotos = false, selectedMemoryPhotoUris = emptyList())
                    }
                    _event.emit(TodayEvent.MemoryPhotosSaved)
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isSavingMemoryPhotos = false, errorMessage = cause.message) }
                    _event.emit(TodayEvent.ShowMessage(cause.message ?: "사진을 저장하지 못했어요."))
                }
        }
    }

    private fun loadTodayCourse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getTodayCourseUseCase()
                .onSuccess { course ->
                    _uiState.update {
                        it.copy(
                            hasCourse = course != null,
                            courseId = course?.courseId,
                            members = course?.members?.map { member ->
                                CourseMember(id = member.nickname, name = member.nickname)
                            }.orEmpty(),
                            places = course?.places?.map { place ->
                                CoursePlace(
                                    id = place.coursePlaceId.toString(),
                                    name = place.placeName,
                                    category = place.category,
                                )
                            }.orEmpty(),
                            isLoading = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                }
        }
    }
}
