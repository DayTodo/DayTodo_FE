package com.team_daytodo.daytodo.domain.course.usecase

import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.InvalidCourseCreateRequestException
import com.team_daytodo.daytodo.domain.course.model.validateForCreateOrNull
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject

class CreateCourseRoomUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(request: CourseCreateRequest): Result<CourseCreateResult> {
        val validationError = request.validateForCreateOrNull()
        if (validationError != null) {
            return Result.failure(validationError)
        }

        return courseRepository.createCourseRoom(request)
    }
}

class JoinCourseUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(inviteCode: String): Result<String> {
        val normalizedInviteCode = inviteCode.trim()
        if (normalizedInviteCode.isBlank()) {
            return Result.failure(
                InvalidCourseCreateRequestException("Invite code is required."),
            )
        }

        return courseRepository.joinCourse(normalizedInviteCode)
    }
}
