package com.team_daytodo.daytodo.data.today

import com.team_daytodo.daytodo.core.model.CommonError
import com.team_daytodo.daytodo.data.api.TodayApi
import com.team_daytodo.daytodo.data.dto.today.ReorderCoursePlacesRequest
import com.team_daytodo.daytodo.data.dto.today.SaveMemoryPhotosRequest
import com.team_daytodo.daytodo.data.dto.today.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
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
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class TodayRepositoryImpl @Inject constructor(
    private val todayApi: TodayApi,
    private val json: Json,
) : TodayRepository {
    override suspend fun getTodayCourse(): Result<TodayCourse?> =
        safeApiResult(json) {
            todayApi.getTodayCourse()
        }.mapCatching {
            it.toDomain()
        }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun completeCourse(courseId: Long): Result<CompletedCourse> =
        safeApiResult(json) {
            todayApi.completeCourse(courseId)
        }.mapCatching {
            it.toDomain()
        }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun saveMemoryPhotos(courseId: Long, imageUrls: List<String>): Result<SavedMemoryPhotos> =
        safeApiResult(json) {
            todayApi.saveMemoryPhotos(courseId, SaveMemoryPhotosRequest(imageUrls))
        }.mapCatching {
            it.toDomain()
        }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun reorderCoursePlaces(
        courseId: Long,
        orderedCoursePlaceIds: List<Long>,
    ): Result<List<TodayCoursePlace>> = safeApiResult(json) {
        todayApi.reorderCoursePlaces(courseId, ReorderCoursePlacesRequest(orderedCoursePlaceIds))
    }.mapCatching {
        it.toDomain()
    }.recoverCatching { cause -> throw cause.toReorderCoursePlacesException() }

    override suspend fun deleteCoursePlace(
        courseId: Long,
        coursePlaceId: Long,
    ): Result<List<TodayCoursePlace>> = safeApiResult(json) {
        todayApi.deleteCoursePlace(courseId, coursePlaceId)
    }.mapCatching {
        it.toDomain()
    }.recoverCatching { cause -> throw cause.toDeleteCoursePlaceException() }
}

private fun Throwable.toTodayCourseException(): Throwable = when {
    this is TodayCourseException -> this
    statusCodeOrNull() == 401 -> TodayUnauthorizedException(this)
    statusCodeOrNull() == 404 -> TodayCourseNotFoundException(this)
    statusCodeOrNull() == 409 -> InvalidCourseStatusException(this)
    statusCodeOrNull() == 400 -> InvalidMemoryPhotoException(this)
    this is CommonError -> this
    else -> TodayCourseLoadException(this)
}

// 404: COURSE_NOT_FOUND 또는 COURSE_PLACE_NOT_FOUND, 400: INVALID_PLACE_ORDER (BE가 코드별 응답 바디를 내려주지 않아 상태코드로만 구분한다)
private fun Throwable.toReorderCoursePlacesException(): Throwable = when {
    this is TodayCourseException -> this
    statusCodeOrNull() == 401 -> TodayUnauthorizedException(this)
    statusCodeOrNull() == 404 -> CoursePlaceNotFoundException(this)
    statusCodeOrNull() == 400 -> InvalidPlaceOrderException(this)
    this is CommonError -> this
    else -> TodayCourseLoadException(this)
}

// 404: COURSE_NOT_FOUND 또는 COURSE_PLACE_NOT_FOUND
private fun Throwable.toDeleteCoursePlaceException(): Throwable = when {
    this is TodayCourseException -> this
    statusCodeOrNull() == 401 -> TodayUnauthorizedException(this)
    statusCodeOrNull() == 404 -> CoursePlaceNotFoundException(this)
    this is CommonError -> this
    else -> TodayCourseLoadException(this)
}

private fun Throwable.statusCodeOrNull(): Int? =
    when (this) {
        is CommonError -> statusCode
        is HttpException -> code()
        else -> null
    }
