package com.team_daytodo.daytodo.domain.course.usecase

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseCommentThread
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.model.InvalidCourseCreateRequestException
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateCourseRoomUseCaseTest {
    @Test
    fun `invalid budget returns failure before repository call`() = runTest {
        val repository = FakeCourseRepository()
        val useCase = CreateCourseRoomUseCase(repository)

        val result = useCase(
            defaultRequest().copy(
                minBudget = 50_000,
                maxBudget = 10_000,
            ),
        )

        assertTrue(result.exceptionOrNull() is InvalidCourseCreateRequestException)
        assertFalse(repository.createCourseRoomCalled)
    }

    @Test
    fun `invalid calendar date returns failure before repository call`() = runTest {
        val repository = FakeCourseRepository()
        val useCase = CreateCourseRoomUseCase(repository)

        val result = useCase(
            defaultRequest().copy(
                date = CourseDate(year = 2026, month = 2, day = 31),
            ),
        )

        assertTrue(result.exceptionOrNull() is InvalidCourseCreateRequestException)
        assertFalse(repository.createCourseRoomCalled)
    }

    @Test
    fun `valid request delegates to repository`() = runTest {
        val repository = FakeCourseRepository()
        val useCase = CreateCourseRoomUseCase(repository)

        val result = useCase(defaultRequest())

        assertTrue(result.isSuccess)
        assertTrue(repository.createCourseRoomCalled)
    }

    private fun defaultRequest(): CourseCreateRequest =
        CourseCreateRequest(
            roomName = "weekend course",
            region = "Seoul",
            date = CourseDate(year = 2026, month = 7, day = 21),
            minBudget = 10_000,
            maxBudget = 50_000,
            relationship = Relationship.FRIEND,
        )

    private class FakeCourseRepository : CourseRepository {
        var createCourseRoomCalled = false
            private set

        override suspend fun getCourseRegions(): Result<List<CourseRegionGroup>> =
            Result.success(emptyList())

        override suspend fun createCourseRoom(
            request: CourseCreateRequest,
        ): Result<CourseCreateResult> {
            createCourseRoomCalled = true
            return Result.success(CourseCreateResult(inviteLink = "https://daytodo.test/invite"))
        }

        override suspend fun joinCourse(inviteCode: String): Result<String> =
            Result.success("Joined course")

        override suspend fun getUpcomingCourses(): Result<List<CourseSummary>> =
            unused()

        override suspend fun getCourseDetail(courseId: String): Result<CourseDetail> =
            unused()

        override suspend fun searchPlaces(
            courseId: String,
            query: String,
        ): Result<List<CoursePlaceSearchResult>> =
            unused()

        override suspend fun togglePlaceLike(
            courseId: String,
            placeId: String,
        ): Result<CourseDetail> =
            unused()

        override suspend fun recommendPlace(
            courseId: String,
            placeId: String,
        ): Result<CourseDetail> =
            unused()

        override suspend fun toggleCoursePlace(
            courseId: String,
            placeId: String,
        ): Result<CourseDetail> =
            unused()

        override suspend fun removeCoursePlace(
            courseId: String,
            placeId: String,
        ): Result<CourseDetail> =
            unused()

        override suspend fun moveCoursePlace(
            courseId: String,
            fromIndex: Int,
            toIndex: Int,
        ): Result<CourseDetail> =
            unused()

        override suspend fun updateCourseSettings(request: CourseUpdateRequest): Result<CourseDetail> =
            unused()

        override suspend fun getPlaceComments(
            courseId: String,
            placeId: String,
        ): Result<CourseCommentThread> =
            unused()

        override suspend fun addPlaceComment(
            courseId: String,
            placeId: String,
            content: String,
        ): Result<CourseCommentThread> =
            unused()

        private fun <T> unused(): Result<T> =
            Result.failure(UnsupportedOperationException("Not used in this test."))
    }
}
