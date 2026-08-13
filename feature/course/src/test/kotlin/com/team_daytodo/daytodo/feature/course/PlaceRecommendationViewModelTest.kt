package com.team_daytodo.daytodo.feature.course

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.course.model.CourseCommentThread
import com.team_daytodo.daytodo.domain.course.model.CourseCoordinate
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CourseMember
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import com.team_daytodo.daytodo.domain.course.usecase.GetCourseDetailUseCase
import com.team_daytodo.daytodo.domain.course.usecase.MoveCoursePlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.RecommendPlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.RemoveCoursePlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.RefreshAiCourseRecommendationsUseCase
import com.team_daytodo.daytodo.domain.course.usecase.SearchPlacesUseCase
import com.team_daytodo.daytodo.domain.course.usecase.ToggleCoursePlaceUseCase
import com.team_daytodo.daytodo.domain.course.usecase.TogglePlaceLikeUseCase
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationUiState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceRecommendationViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `searchPlaces keeps latest result when responses complete out of order`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val repository = FakeCourseRepository()
            val viewModel = createViewModel(repository)

            viewModel.loadCourse(CourseId)
            advanceUntilIdle()

            viewModel.updateSearchQuery("old")
            viewModel.searchPlaces()
            runCurrent()

            viewModel.updateSearchQuery("new")
            viewModel.searchPlaces()
            runCurrent()

            repository.completeSearch("new", listOf(searchResult("new-place")))
            advanceUntilIdle()

            assertEquals(listOf("new-place"), viewModel.uiState.value.searchResultPlaceIds())
            assertEquals("new-place", viewModel.uiState.value.selectedPlaceId)

            repository.completeSearch("old", listOf(searchResult("old-place")))
            advanceUntilIdle()

            assertEquals(listOf("new-place"), viewModel.uiState.value.searchResultPlaceIds())
            assertEquals("new-place", viewModel.uiState.value.selectedPlaceId)
        }

    @Test
    fun `searchPlaces ignores in-flight result after search is cleared`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val repository = FakeCourseRepository()
            val viewModel = createViewModel(repository)

            viewModel.loadCourse(CourseId)
            advanceUntilIdle()

            viewModel.updateSearchQuery("old")
            viewModel.searchPlaces()
            runCurrent()

            viewModel.clearSearch()
            repository.completeSearch("old", listOf(searchResult("old-place")))
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.searchResults.isEmpty())
            assertFalse(viewModel.uiState.value.searchPerformed)
        }

    private fun createViewModel(repository: CourseRepository): PlaceRecommendationViewModel =
        PlaceRecommendationViewModel(
            getCourseDetailUseCase = GetCourseDetailUseCase(repository),
            searchPlacesUseCase = SearchPlacesUseCase(repository),
            togglePlaceLikeUseCase = TogglePlaceLikeUseCase(repository),
            recommendPlaceUseCase = RecommendPlaceUseCase(repository),
            toggleCoursePlaceUseCase = ToggleCoursePlaceUseCase(repository),
            removeCoursePlaceUseCase = RemoveCoursePlaceUseCase(repository),
            moveCoursePlaceUseCase = MoveCoursePlaceUseCase(repository),
            refreshAiCourseRecommendationsUseCase = RefreshAiCourseRecommendationsUseCase(repository),
        )

    private fun searchResult(placeId: String): CoursePlaceSearchResult =
        CoursePlaceSearchResult(
            place = coursePlace(placeId),
            recommendedByCurrentMember = false,
            isInCourse = false,
        )

    private fun coursePlace(placeId: String): CoursePlace =
        CoursePlace(
            id = placeId,
            name = "$placeId name",
            address = "Seoul",
            category = "Cafe",
            description = "Test place",
            expectedPrice = 10_000,
            coordinate = CourseCoordinate(latitude = 37.0, longitude = 127.0),
        )

    private fun PlaceRecommendationUiState.searchResultPlaceIds(): List<String> =
        searchResults.map { it.place.id }

    private fun defaultCourseDetail(courseId: String): CourseDetail =
        CourseDetail(
            id = courseId,
            name = "Test course",
            region = "Seoul",
            regionCoordinate = CourseCoordinate(latitude = 37.0, longitude = 127.0),
            date = CourseDate(year = 2026, month = 7, day = 26),
            minBudget = 10_000,
            maxBudget = 50_000,
            relationship = Relationship.FRIEND,
            currentMemberId = "member-1",
            members = listOf(CourseMember(id = "member-1", name = "Me")),
            recommendedPlaces = emptyList(),
            coursePlaces = emptyList(),
        )

    private inner class FakeCourseRepository : CourseRepository {
        private val pendingSearches =
            mutableMapOf<String, CompletableDeferred<Result<List<CoursePlaceSearchResult>>>>()

        fun completeSearch(
            query: String,
            results: List<CoursePlaceSearchResult>,
        ) {
            val pendingSearch = checkNotNull(pendingSearches[query]) {
                "No pending search for query: $query"
            }
            pendingSearch.complete(Result.success(results))
        }

        override suspend fun getCourseRegions(): Result<List<CourseRegionGroup>> =
            unused()

        override suspend fun createCourseRoom(request: CourseCreateRequest): Result<CourseCreateResult> =
            unused()

        override suspend fun joinCourse(inviteCode: String): Result<String> =
            unused()

        override suspend fun getUpcomingCourses(
            startDate: CourseDate?,
            endDate: CourseDate?,
        ): Result<List<CourseSummary>> =
            unused()

        override suspend fun getCourseDetail(courseId: String): Result<CourseDetail> =
            Result.success(defaultCourseDetail(courseId))

        override suspend fun refreshAiCourseRecommendations(courseId: String): Result<CourseDetail> =
            Result.success(defaultCourseDetail(courseId))

        override suspend fun searchPlaces(
            courseId: String,
            query: String,
        ): Result<List<CoursePlaceSearchResult>> =
            pendingSearches.getOrPut(query) {
                CompletableDeferred()
            }.await()

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

    class MainDispatcherRule(
        val testDispatcher: TestDispatcher = StandardTestDispatcher(),
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }

    private companion object {
        const val CourseId = "course-1"
    }
}
