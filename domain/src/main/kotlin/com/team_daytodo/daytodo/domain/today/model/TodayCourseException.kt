package com.team_daytodo.daytodo.domain.today.model

sealed class TodayCourseException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)

class TodayCourseNotFoundException(cause: Throwable? = null) : TodayCourseException(
    message = "코스를 찾을 수 없어요.",
    cause = cause,
)

class InvalidCourseStatusException(cause: Throwable? = null) : TodayCourseException(
    message = "진행 중인 코스만 종료할 수 있어요.",
    cause = cause,
)

class InvalidMemoryPhotoException(cause: Throwable? = null) : TodayCourseException(
    message = "저장할 사진이 없어요.",
    cause = cause,
)

class TodayUnauthorizedException(cause: Throwable? = null) : TodayCourseException(
    message = "로그인이 필요해요.",
    cause = cause,
)

class TodayCourseLoadException(cause: Throwable? = null) : TodayCourseException(
    message = "오늘의 코스 정보를 불러오지 못했어요.",
    cause = cause,
)

class InvalidPlaceOrderException(cause: Throwable? = null) : TodayCourseException(
    message = "순서 정보가 올바르지 않아요.",
    cause = cause,
)

class CoursePlaceNotFoundException(cause: Throwable? = null) : TodayCourseException(
    message = "코스에 속하지 않는 장소가 포함되어 있어요.",
    cause = cause,
)
