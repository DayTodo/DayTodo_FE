package com.team_daytodo.daytodo.domain.course.usecase

import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.InvalidCourseCreateRequestException
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject

class CreateCourseRoomUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(request: CourseCreateRequest): Result<CourseCreateResult> {
        val validationError = request.validateOrNull()
        if (validationError != null) {
            return Result.failure(validationError)
        }

        return courseRepository.createCourseRoom(request)
    }
}

private fun CourseCreateRequest.validateOrNull(): InvalidCourseCreateRequestException? =
    when {
        roomName.isBlank() -> InvalidCourseCreateRequestException("코스 방 이름을 입력해 주세요.")
        region.isBlank() -> InvalidCourseCreateRequestException("코스 지역을 선택해 주세요.")
        !date.isValid() -> InvalidCourseCreateRequestException("올바른 날짜를 선택해 주세요.")
        minBudget < 0 || maxBudget < 0 -> InvalidCourseCreateRequestException("예산은 0원 이상으로 입력해 주세요.")
        minBudget > maxBudget -> InvalidCourseCreateRequestException("최소 예산은 최대 예산보다 클 수 없습니다.")
        else -> null
    }

private fun CourseDate.isValid(): Boolean {
    if (year < 1 || month !in 1..12) return false

    return day in 1..daysInMonth(year, month)
}

private fun daysInMonth(year: Int, month: Int): Int =
    when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year.isLeapYear()) 29 else 28
        else -> 0
    }

private fun Int.isLeapYear(): Boolean =
    this % 4 == 0 && (this % 100 != 0 || this % 400 == 0)
