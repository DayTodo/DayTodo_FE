package com.team_daytodo.daytodo.data.today

import com.team_daytodo.daytodo.data.api.TodayApi
import com.team_daytodo.daytodo.data.dto.today.ReorderCoursePlacesRequest
import com.team_daytodo.daytodo.data.dto.today.SaveMemoryPhotosRequest
import com.team_daytodo.daytodo.data.dto.today.toDomain
import com.team_daytodo.daytodo.domain.today.model.CompletedCourse
import com.team_daytodo.daytodo.domain.today.model.CoursePlaceNotFoundException
import com.team_daytodo.daytodo.domain.today.model.InvalidCourseStatusException
import com.team_daytodo.daytodo.domain.today.model.InvalidMemoryPhotoException
import com.team_daytodo.daytodo.domain.today.model.InvalidPlaceOrderException
import com.team_daytodo.daytodo.domain.today.model.SavedMemoryPhotos
import com.team_daytodo.daytodo.domain.today.model.TodayCourse
import com.team_daytodo.daytodo.domain.today.model.TodayCourseException
import com.team_daytodo.daytodo.domain.today.model.TodayCourseLoadException
import com.team_daytodo.daytodo.domain.today.model.TodayCourseNotFoundException
import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import com.team_daytodo.daytodo.domain.today.model.TodayUnauthorizedException
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject
import retrofit2.HttpException

class TodayRepositoryImpl @Inject constructor(
    private val todayApi: TodayApi,
) : TodayRepository {
    override suspend fun getTodayCourse(): Result<TodayCourse?> = runCatching {
        todayApi.getTodayCourse().toDomain()
    }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun completeCourse(courseId: Long): Result<CompletedCourse> = runCatching {
        todayApi.completeCourse(courseId).toDomain()
    }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun saveMemoryPhotos(courseId: Long, imageUrls: List<String>): Result<SavedMemoryPhotos> =
        runCatching {
            todayApi.saveMemoryPhotos(courseId, SaveMemoryPhotosRequest(imageUrls)).toDomain()
        }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun reorderCoursePlaces(
        courseId: Long,
        orderedCoursePlaceIds: List<Long>,
    ): Result<List<TodayCoursePlace>> = runCatching {
        todayApi.reorderCoursePlaces(courseId, ReorderCoursePlacesRequest(orderedCoursePlaceIds)).toDomain()
    }.recoverCatching { cause -> throw cause.toReorderCoursePlacesException() }

    override suspend fun deleteCoursePlace(
        courseId: Long,
        coursePlaceId: Long,
    ): Result<List<TodayCoursePlace>> = runCatching {
        todayApi.deleteCoursePlace(courseId, coursePlaceId).toDomain()
    }.recoverCatching { cause -> throw cause.toDeleteCoursePlaceException() }
}

private fun Throwable.toTodayCourseException(): TodayCourseException = when {
    this is TodayCourseException -> this
    this is HttpException && code() == 401 -> TodayUnauthorizedException(this)
    this is HttpException && code() == 404 -> TodayCourseNotFoundException(this)
    this is HttpException && code() == 409 -> InvalidCourseStatusException(this)
    this is HttpException && code() == 400 -> InvalidMemoryPhotoException(this)
    else -> TodayCourseLoadException(this)
}

// 404: COURSE_NOT_FOUND 또는 COURSE_PLACE_NOT_FOUND, 400: INVALID_PLACE_ORDER (BE가 코드별 응답 바디를 내려주지 않아 상태코드로만 구분한다)
private fun Throwable.toReorderCoursePlacesException(): TodayCourseException = when {
    this is TodayCourseException -> this
    this is HttpException && code() == 401 -> TodayUnauthorizedException(this)
    this is HttpException && code() == 404 -> CoursePlaceNotFoundException(this)
    this is HttpException && code() == 400 -> InvalidPlaceOrderException(this)
    else -> TodayCourseLoadException(this)
}

// 404: COURSE_NOT_FOUND 또는 COURSE_PLACE_NOT_FOUND
private fun Throwable.toDeleteCoursePlaceException(): TodayCourseException = when {
    this is TodayCourseException -> this
    this is HttpException && code() == 401 -> TodayUnauthorizedException(this)
    this is HttpException && code() == 404 -> CoursePlaceNotFoundException(this)
    else -> TodayCourseLoadException(this)
}
