package com.team_daytodo.daytodo.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.record.usecase.AddRecordMemoUseCase
import com.team_daytodo.daytodo.domain.record.usecase.GetRecordCalendarUseCase
import com.team_daytodo.daytodo.feature.record.model.MemoEntry
import com.team_daytodo.daytodo.feature.record.model.RecordPhoto
import com.team_daytodo.daytodo.feature.record.model.RecordUiState
import com.team_daytodo.daytodo.feature.record.model.VisitedCourse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getRecordCalendarUseCase: GetRecordCalendarUseCase,
    private val addRecordMemoUseCase: AddRecordMemoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        loadCalendar()
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun showPreviousMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.minusMonths(1)) }
    }

    fun showNextMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.plusMonths(1)) }
    }

    fun addMemo(photoId: String, content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            addRecordMemoUseCase(photoId = photoId, author = SelfAuthor, content = content)
                .onSuccess { memo ->
                    val entry = MemoEntry(id = memo.id, author = memo.author, content = memo.content)
                    _uiState.update { state ->
                        state.copy(
                            memosByPhotoId = state.memosByPhotoId +
                                (photoId to (state.memosOf(photoId) + entry)),
                        )
                    }
                }
        }
    }

    private fun loadCalendar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getRecordCalendarUseCase()
                .onSuccess { calendar ->
                    val defaultSelectedDate = calendar.courseDates.maxOrNull() ?: LocalDate.now()

                    _uiState.update {
                        it.copy(
                            selectedDate = defaultSelectedDate,
                            currentMonth = YearMonth.from(defaultSelectedDate),
                            courseDates = calendar.courseDates,
                            photosByDate = calendar.photoCountsByDate.mapValues { (date, count) ->
                                photosFor(date, count)
                            },
                            coursesByDate = calendar.coursesByDate.mapValues { (_, courses) ->
                                courses.map { VisitedCourse(id = it.id, title = it.title) }
                            },
                            memosByPhotoId = calendar.memosByPhotoId.mapValues { (_, memos) ->
                                memos.map { MemoEntry(id = it.id, author = it.author, content = it.content) }
                            },
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    // 미리보기용 더미 사진 3종을 날짜별 사진 개수만큼 순환시켜 사용한다.
    private fun photosFor(date: LocalDate, count: Int): List<RecordPhoto> {
        val idPrefix = date.format(PhotoIdDateFormatter)
        return List(count) { index ->
            RecordPhoto(
                id = "$idPrefix-$index",
                imageRes = dummyPhotoResources[index % dummyPhotoResources.size],
            )
        }
    }

    private companion object {
        const val SelfAuthor = "나"
        val PhotoIdDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMdd")
        val dummyPhotoResources = listOf(
            R.drawable.dummypicture_1,
            R.drawable.dummypicture_2,
            R.drawable.dummypicture_3,
        )
    }
}
