package com.team_daytodo.daytodo.feature.record

import androidx.lifecycle.ViewModel
import com.team_daytodo.daytodo.feature.record.model.MemoEntry
import com.team_daytodo.daytodo.feature.record.model.RecordUiState
import com.team_daytodo.daytodo.feature.record.model.sampleRecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

@HiltViewModel
class RecordViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(sampleRecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private var nextLocalMemoId = 0

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun showPreviousMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.minusMonths(1)) }
    }

    fun showNextMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.plusMonths(1)) }
    }

    /** 메모는 사진 id 기준으로 저장되므로 인덱스가 바뀌어도 해당 사진에만 반영된다. */
    fun addMemo(photoId: String, content: String) {
        if (content.isBlank()) return

        val memo = MemoEntry(
            id = "local-${nextLocalMemoId++}",
            author = "나",
            content = content,
        )
        _uiState.update { state ->
            state.copy(
                memosByPhotoId = state.memosByPhotoId +
                    (photoId to (state.memosOf(photoId) + memo)),
            )
        }
    }
}
