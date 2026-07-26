package com.team_daytodo.daytodo.domain.course.usecase

import com.team_daytodo.daytodo.domain.course.model.CourseCommentThread
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.model.InvalidCourseEditRequestException
import com.team_daytodo.daytodo.domain.course.model.validateForUpdateOrNull
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject

class GetUpcomingCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(): Result<List<CourseSummary>> =
        courseRepository.getUpcomingCourses()
}

class GetCourseDetailUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(courseId: String): Result<CourseDetail> =
        courseRepository.getCourseDetail(courseId)
}

class SearchPlacesUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        query: String,
    ): Result<List<CoursePlaceSearchResult>> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return Result.success(emptyList())

        return courseRepository.searchPlaces(
            courseId = courseId,
            query = normalizedQuery,
        )
    }
}

class TogglePlaceLikeUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        courseRepository.togglePlaceLike(courseId, placeId)
}

class RecommendPlaceUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        courseRepository.recommendPlace(courseId, placeId)
}

class ToggleCoursePlaceUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        courseRepository.toggleCoursePlace(courseId, placeId)
}

class RemoveCoursePlaceUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        courseRepository.removeCoursePlace(courseId, placeId)
}

class MoveCoursePlaceUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        fromIndex: Int,
        toIndex: Int,
    ): Result<CourseDetail> =
        courseRepository.moveCoursePlace(courseId, fromIndex, toIndex)
}

class UpdateCourseSettingsUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(request: CourseUpdateRequest): Result<CourseDetail> {
        val validationError = request.validateForUpdateOrNull()
        if (validationError != null) {
            return Result.failure(validationError)
        }

        return courseRepository.updateCourseSettings(request)
    }
}

class GetPlaceCommentsUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        placeId: String,
    ): Result<CourseCommentThread> =
        courseRepository.getPlaceComments(courseId, placeId)
}

class AddPlaceCommentUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
) {
    suspend operator fun invoke(
        courseId: String,
        placeId: String,
        content: String,
    ): Result<CourseCommentThread> {
        val normalizedContent = content.trim()
        if (normalizedContent.isBlank()) {
            return Result.failure(InvalidCourseEditRequestException("댓글 내용을 입력해 주세요."))
        }

        return courseRepository.addPlaceComment(
            courseId = courseId,
            placeId = placeId,
            content = normalizedContent,
        )
    }
}
