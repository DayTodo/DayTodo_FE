package com.team_daytodo.daytodo.data.today

import com.team_daytodo.daytodo.data.api.TodayApi
import com.team_daytodo.daytodo.data.dto.today.SaveMemoryPhotosRequest
import com.team_daytodo.daytodo.domain.today.model.CompletedCourse
import com.team_daytodo.daytodo.domain.today.model.InvalidCourseStatusException
import com.team_daytodo.daytodo.domain.today.model.InvalidMemoryPhotoException
import com.team_daytodo.daytodo.domain.today.model.MemoryPhoto
import com.team_daytodo.daytodo.domain.today.model.SavedMemoryPhotos
import com.team_daytodo.daytodo.domain.today.model.TodayCourse
import com.team_daytodo.daytodo.domain.today.model.TodayCourseException
import com.team_daytodo.daytodo.domain.today.model.TodayCourseLoadException
import com.team_daytodo.daytodo.domain.today.model.TodayCourseMember
import com.team_daytodo.daytodo.domain.today.model.TodayCourseNotFoundException
import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import com.team_daytodo.daytodo.domain.today.model.TodayUnauthorizedException
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import java.time.LocalDateTime
import javax.inject.Inject
import retrofit2.HttpException

class TodayRepositoryImpl @Inject constructor(
    private val todayApi: TodayApi,
) : TodayRepository {
    override suspend fun getTodayCourse(): Result<TodayCourse?> = runCatching {
        todayApi.getTodayCourse().todayCourse?.let { dto ->
            TodayCourse(
                courseId = dto.courseId,
                courseName = dto.courseName,
                members = dto.members.map {
                    TodayCourseMember(nickname = it.nickname, profileImageUrl = it.profileImageUrl)
                },
                places = dto.places.map {
                    TodayCoursePlace(
                        coursePlaceId = it.coursePlaceId,
                        placeOrder = it.placeOrder,
                        placeName = it.placeName,
                        category = it.category,
                    )
                },
            )
        }
    }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun completeCourse(courseId: Long): Result<CompletedCourse> = runCatching {
        val response = todayApi.completeCourse(courseId)
        CompletedCourse(
            courseId = response.courseId,
            courseStatus = response.courseStatus,
            completedAt = LocalDateTime.parse(response.completedAt),
        )
    }.recoverCatching { cause -> throw cause.toTodayCourseException() }

    override suspend fun saveMemoryPhotos(courseId: Long, imageUrls: List<String>): Result<SavedMemoryPhotos> =
        runCatching {
            val response = todayApi.saveMemoryPhotos(courseId, SaveMemoryPhotosRequest(imageUrls))
            SavedMemoryPhotos(
                savedCount = response.savedCount,
                photos = response.photos.map {
                    MemoryPhoto(
                        memoryPhotoId = it.memoryPhotoId,
                        imageUrl = it.imageUrl,
                        photoOrder = it.photoOrder,
                    )
                },
            )
        }.recoverCatching { cause -> throw cause.toTodayCourseException() }
}

private fun Throwable.toTodayCourseException(): TodayCourseException = when {
    this is TodayCourseException -> this
    this is HttpException && code() == 401 -> TodayUnauthorizedException(this)
    this is HttpException && code() == 404 -> TodayCourseNotFoundException(this)
    this is HttpException && code() == 409 -> InvalidCourseStatusException(this)
    this is HttpException && code() == 400 -> InvalidMemoryPhotoException(this)
    else -> TodayCourseLoadException(this)
}
