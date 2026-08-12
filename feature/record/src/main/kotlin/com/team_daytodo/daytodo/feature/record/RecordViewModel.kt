package com.team_daytodo.daytodo.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.calendar.usecase.GetCalendarCoursesUseCase
import com.team_daytodo.daytodo.domain.record.usecase.GetRecordCoursePlacesUseCase
import com.team_daytodo.daytodo.domain.record.usecase.GetRecordDiaryUseCase
import com.team_daytodo.daytodo.domain.record.usecase.GetRecordPhotosUseCase
import com.team_daytodo.daytodo.domain.record.usecase.RemoveRecordPlaceBookmarkUseCase
import com.team_daytodo.daytodo.domain.record.usecase.SaveRecordPlaceBookmarkUseCase
import com.team_daytodo.daytodo.domain.record.usecase.WriteRecordDiaryUseCase
import com.team_daytodo.daytodo.feature.record.model.RecordEvent
import com.team_daytodo.daytodo.feature.record.model.RecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getCalendarCoursesUseCase: GetCalendarCoursesUseCase,
    private val getRecordCoursePlacesUseCase: GetRecordCoursePlacesUseCase,
    private val getRecordPhotosUseCase: GetRecordPhotosUseCase,
    private val getRecordDiaryUseCase: GetRecordDiaryUseCase,
    private val writeRecordDiaryUseCase: WriteRecordDiaryUseCase,
    private val saveRecordPlaceBookmarkUseCase: SaveRecordPlaceBookmarkUseCase,
    private val removeRecordPlaceBookmarkUseCase: RemoveRecordPlaceBookmarkUseCase,
) : ViewModel() {
    private val today = LocalDate.now()

    private val _uiState = MutableStateFlow(
        RecordUiState(
            currentYear = today.year,
            currentMonth = today.monthValue,
            selectedDate = today,
        ),
    )
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<RecordEvent>()
    val event: SharedFlow<RecordEvent> = _event.asSharedFlow()

    // onMonthChanged의 최초 진입 호출과 이후 월 이동 호출을 모두 이 값으로 구분한다.
    private var lastRequestedMonth: YearMonth? = null

    /**
     * 기록 캘린더 진입 및 이후 모든 월 이동(이전/다음 버튼)의 단일 진입점.
     * RecordScreen의 LaunchedEffect(Unit)이 최초 진입 시 한 번 호출해 초기 로드를 겸한다.
     */
    fun onMonthChanged(yearMonth: YearMonth) {
        if (yearMonth == lastRequestedMonth) return
        lastRequestedMonth = yearMonth

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentYear = yearMonth.year,
                    currentMonth = yearMonth.monthValue,
                    isLoading = true,
                    errorMessage = null,
                )
            }
            getCalendarCoursesUseCase(yearMonth.year, yearMonth.monthValue)
                .onSuccess { calendarDates ->
                    _uiState.update {
                        it.copy(
                            coursesByDate = calendarDates.associate { it.date to it.courses },
                            isLoading = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = cause.message) }
                    _event.emit(RecordEvent.ShowMessage(cause.message ?: "캘린더를 불러오지 못했어요."))
                }
        }
    }

    fun showPreviousMonth() {
        val current = YearMonth.of(_uiState.value.currentYear, _uiState.value.currentMonth)
        onMonthChanged(current.minusMonths(1))
    }

    fun showNextMonth() {
        val current = YearMonth.of(_uiState.value.currentYear, _uiState.value.currentMonth)
        onMonthChanged(current.plusMonths(1))
    }

    fun onDateSelected(date: LocalDate) {
        val courses = _uiState.value.coursesByDate[date].orEmpty()
        _uiState.update {
            it.copy(
                selectedDate = date,
                selectedCourseId = null,
                places = emptyList(),
                photos = emptyList(),
                diaryContent = "",
            )
        }
        if (courses.size == 1) {
            onCourseSelected(courses.first().courseId)
        }
    }

    fun onCourseSelected(courseId: Long) {
        _uiState.update { it.copy(selectedCourseId = courseId, isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val date = _uiState.value.selectedDate

            val (placesResult, photosResult, diaryResult) = coroutineScope {
                val places = async { getRecordCoursePlacesUseCase(courseId) }
                val photos = async { getRecordPhotosUseCase(courseId) }
                val diary = async { getRecordDiaryUseCase(date) }
                Triple(places.await(), photos.await(), diary.await())
            }

            val failure = placesResult.exceptionOrNull()
                ?: photosResult.exceptionOrNull()
                ?: diaryResult.exceptionOrNull()

            _uiState.update { state ->
                state.copy(
                    places = placesResult.getOrNull() ?: state.places,
                    photos = photosResult.getOrNull() ?: state.photos,
                    diaryContent = diaryResult.map { diary -> diary?.content ?: "" }.getOrDefault(state.diaryContent),
                    isLoading = false,
                    errorMessage = failure?.message,
                )
            }
            if (failure != null) {
                _event.emit(RecordEvent.ShowMessage(failure.message ?: "기록을 불러오지 못했어요."))
            }
        }
    }

    fun onDiaryContentChanged(content: String) {
        _uiState.update { it.copy(diaryContent = content) }
    }

    fun onSaveDiaryClick() {
        val courseId = _uiState.value.selectedCourseId ?: return
        val content = _uiState.value.diaryContent

        viewModelScope.launch {
            writeRecordDiaryUseCase(courseId, content)
                .onSuccess { diary -> _uiState.update { it.copy(diaryContent = diary.content) } }
                .onFailure { cause ->
                    _uiState.update { it.copy(errorMessage = cause.message) }
                    _event.emit(RecordEvent.ShowMessage(cause.message ?: "일기를 저장하지 못했어요."))
                }
        }
    }

    fun onSavePlaceClick(placeId: Long) {
        val state = _uiState.value
        if (placeId in state.pendingBookmarkPlaceIds) return

        if (placeId in state.savedPlaceIds) {
            val bookmarkId = state.placeBookmarkIds[placeId] ?: return
            removePlaceBookmark(placeId, bookmarkId)
        } else {
            savePlaceBookmark(placeId)
        }
    }

    // 저장은 즉시 "저장됨"으로 낙관적 표시하고, 실패하면 롤백한다.
    // 삭제(DELETE)에 필요한 진짜 bookmarkId는 서버 응답이 와야 알 수 있으므로,
    // 응답 전까지는 pendingBookmarkPlaceIds로 같은 placeId의 중복 탭을 막는다.
    private fun savePlaceBookmark(placeId: Long) {
        _uiState.update {
            it.copy(
                savedPlaceIds = it.savedPlaceIds + placeId,
                pendingBookmarkPlaceIds = it.pendingBookmarkPlaceIds + placeId,
            )
        }
        viewModelScope.launch {
            saveRecordPlaceBookmarkUseCase(placeId)
                .onSuccess { bookmark ->
                    _uiState.update {
                        it.copy(
                            placeBookmarkIds = it.placeBookmarkIds + (placeId to bookmark.bookmarkId),
                            pendingBookmarkPlaceIds = it.pendingBookmarkPlaceIds - placeId,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            savedPlaceIds = it.savedPlaceIds - placeId,
                            pendingBookmarkPlaceIds = it.pendingBookmarkPlaceIds - placeId,
                            errorMessage = cause.message,
                        )
                    }
                    _event.emit(RecordEvent.ShowMessage(cause.message ?: "장소를 저장하지 못했어요."))
                }
        }
    }

    // 해제는 이미 확정된 bookmarkId가 있으므로 즉시 "저장 해제"로 낙관적 표시하고, 실패하면 롤백한다.
    private fun removePlaceBookmark(placeId: Long, bookmarkId: Long) {
        _uiState.update {
            it.copy(
                savedPlaceIds = it.savedPlaceIds - placeId,
                pendingBookmarkPlaceIds = it.pendingBookmarkPlaceIds + placeId,
            )
        }
        viewModelScope.launch {
            removeRecordPlaceBookmarkUseCase(bookmarkId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            placeBookmarkIds = it.placeBookmarkIds - placeId,
                            pendingBookmarkPlaceIds = it.pendingBookmarkPlaceIds - placeId,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(
                            savedPlaceIds = it.savedPlaceIds + placeId,
                            pendingBookmarkPlaceIds = it.pendingBookmarkPlaceIds - placeId,
                            errorMessage = cause.message,
                        )
                    }
                    _event.emit(RecordEvent.ShowMessage(cause.message ?: "저장을 취소하지 못했어요."))
                }
        }
    }
}
