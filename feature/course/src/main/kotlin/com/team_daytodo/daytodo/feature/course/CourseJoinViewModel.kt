package com.team_daytodo.daytodo.feature.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.course.usecase.JoinCourseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CourseJoinViewModel @Inject constructor(
    private val joinCourseUseCase: JoinCourseUseCase,
) : ViewModel() {
    private val _event = MutableSharedFlow<CourseJoinEvent>()
    val event: SharedFlow<CourseJoinEvent> = _event.asSharedFlow()

    private var isJoining = false

    fun joinCourse(inviteCode: String) {
        if (isJoining) return

        viewModelScope.launch {
            isJoining = true
            joinCourseUseCase(inviteCode)
                .onSuccess {
                    _event.emit(CourseJoinEvent.Joined)
                }
                .onFailure { cause ->
                    _event.emit(CourseJoinEvent.ShowMessage(cause.userFacingMessage()))
                }
            isJoining = false
        }
    }
}

sealed interface CourseJoinEvent {
    data object Joined : CourseJoinEvent
    data class ShowMessage(val message: String) : CourseJoinEvent
}

private fun Throwable.userFacingMessage(): String =
    message?.takeIf(String::isNotBlank) ?: "Failed to join course."
