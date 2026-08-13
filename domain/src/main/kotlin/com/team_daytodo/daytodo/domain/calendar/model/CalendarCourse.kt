package com.team_daytodo.daytodo.domain.calendar.model

data class CalendarCourse(
    val courseId: Long,
    val courseName: String,
    val participantType: ParticipantType,
    val memberCount: Long,
    val courseStatus: CourseStatus,
)
