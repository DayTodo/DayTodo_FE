package com.team_daytodo.daytodo.data.dto.calendar

import com.team_daytodo.daytodo.domain.calendar.model.CalendarCourse
import com.team_daytodo.daytodo.domain.calendar.model.CalendarDate
import com.team_daytodo.daytodo.domain.calendar.model.CourseStatus
import com.team_daytodo.daytodo.domain.calendar.model.ParticipantType
import java.time.LocalDate
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class CalendarResponseDto(
    val year: Int,
    val month: Int,
    val schedules: List<CalendarDateDto>,
)

@Serializable
data class CalendarDateDto(
    val date: String,
    val courses: List<CalendarCourseDto>,
)

@Serializable
data class CalendarCourseDto(
    val courseId: Long,
    val courseName: String,
    val participantType: String,
    val memberCount: Long,
    val courseStatus: String,
)

fun CalendarResponseDto.toDomain(): List<CalendarDate> = schedules.map { it.toDomain() }

fun CalendarDateDto.toDomain(): CalendarDate = CalendarDate(
    date = LocalDate.parse(date),
    courses = courses.mapNotNull { it.toDomainOrNull() },
)

private fun CalendarCourseDto.toDomainOrNull(): CalendarCourse? {
    val type = participantType.toParticipantTypeOrNull() ?: run {
        Timber.w("Unknown ParticipantType '%s' for courseId=%d, skipping course", participantType, courseId)
        return null
    }
    val status = courseStatus.toCourseStatusOrNull() ?: run {
        Timber.w("Unknown CourseStatus '%s' for courseId=%d, skipping course", courseStatus, courseId)
        return null
    }
    return CalendarCourse(
        courseId = courseId,
        courseName = courseName,
        participantType = type,
        memberCount = memberCount,
        courseStatus = status,
    )
}

private fun String.toParticipantTypeOrNull(): ParticipantType? =
    runCatching { ParticipantType.valueOf(this) }.getOrNull()

private fun String.toCourseStatusOrNull(): CourseStatus? =
    runCatching { CourseStatus.valueOf(this) }.getOrNull()
