package com.team_daytodo.daytodo.feature.today.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.today.usecase.CompleteCourseUseCase
import com.team_daytodo.daytodo.domain.today.usecase.GetTodayCourseUseCase
import com.team_daytodo.daytodo.domain.today.usecase.SaveMemoryPhotosUseCase
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace
import com.team_daytodo.daytodo.feature.today.state.TodayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    // TODO: 사진 업로드(로컬 갤러리 -> URL 발급) 파이프라인이 아직 없어 UI 트리거가 연결돼 있지 않다.
    // 이미 URL을 확보한 상태에서 저장만 하고 싶을 때 이 함수를 호출하면 된다.
    fun saveMemoryPhotos(imageUrls: List<String>) {
        val courseId = _uiState.value.courseId ?: return
        if (imageUrls.isEmpty()) return

        viewModelScope.launch {
            saveMemoryPhotosUseCase(courseId, imageUrls)
                .onFailure { cause -> _uiState.update { it.copy(errorMessage = cause.message) } }
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
                            members = course?.members?.mapIndexed { index, member ->
                                CourseMember(id = "member-$index", name = member.nickname)
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
