package com.team_daytodo.daytodo.domain.course.model

sealed class CourseException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

class InvalidCourseCreateRequestException(
    message: String,
) : CourseException(message)

class InvalidCourseEditRequestException(
    message: String,
) : CourseException(message)

class CourseRegionLoadException(
    cause: Throwable? = null,
) : CourseException(
    message = "지역 정보를 불러오지 못했습니다.",
    cause = cause,
)

class UnknownCourseRegionException(
    region: String,
) : CourseException(
    message = "'$region' 지역은 아직 지원하지 않아요. 지역을 다시 선택해 주세요.",
)

class CourseRoomCreateException(
    cause: Throwable? = null,
) : CourseException(
    message = "코스 방을 생성하지 못했습니다.",
    cause = cause,
)
