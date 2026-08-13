package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.data.course.mapper.successMessage
import com.team_daytodo.daytodo.data.course.mapper.toComment
import com.team_daytodo.daytodo.data.course.mapper.toCommentDto
import com.team_daytodo.daytodo.data.course.mapper.toCourseIdLong
import com.team_daytodo.daytodo.data.course.mapper.toDomain
import com.team_daytodo.daytodo.data.course.mapper.toDto
import com.team_daytodo.daytodo.data.course.mapper.toJoinCourseDto
import com.team_daytodo.daytodo.data.course.mapper.toMapRecommendationDto
import com.team_daytodo.daytodo.data.course.mapper.toPlace
import com.team_daytodo.daytodo.data.course.mapper.toPlaceIdLong
import com.team_daytodo.daytodo.data.course.remote.CourseRemoteDataSource
import com.team_daytodo.daytodo.data.course.remote.dto.CourseRecommendationDto
import com.team_daytodo.daytodo.domain.course.model.CourseComment
import com.team_daytodo.daytodo.domain.course.model.CourseCommentThread
import com.team_daytodo.daytodo.domain.course.model.CourseCoordinate
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseDetail
import com.team_daytodo.daytodo.domain.course.model.CourseException
import com.team_daytodo.daytodo.domain.course.model.CourseMember
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseRegionGroup
import com.team_daytodo.daytodo.domain.course.model.CourseRegionLoadException
import com.team_daytodo.daytodo.domain.course.model.CourseRoomCreateException
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.model.PlaceRecommender
import com.team_daytodo.daytodo.domain.course.model.UnknownCourseRegionException
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val remoteDataSource: CourseRemoteDataSource,
    private val localCourseRegionDataSource: LocalCourseRegionDataSource,
) : CourseRepository {
    private val selectedCoursePlaceIdsByCourseId = mutableMapOf<Long, List<String>>()
    private val commentsByCoursePlaceKey = mutableMapOf<CoursePlaceKey, List<CourseComment>>()

    override suspend fun getCourseRegions(): Result<List<CourseRegionGroup>> =
        runCatching {
            localCourseRegionDataSource.getRegions()
        }.recoverCatching { cause ->
            throw CourseRegionLoadException(cause)
        }

    override suspend fun createCourseRoom(request: CourseCreateRequest): Result<CourseCreateResult> =
        runCatching {
            val regionId = requireRegionId(request.region)
            remoteDataSource.createCourse(request.toDto(regionId)).toDomain()
        }.recoverCatching { cause ->
            throw cause.toCourseCreateException()
        }

    override suspend fun joinCourse(inviteCode: String): Result<String> =
        runCatching {
            remoteDataSource.joinCourse(inviteCode.toJoinCourseDto()).successMessage()
        }

    override suspend fun getUpcomingCourses(): Result<List<CourseSummary>> =
        runCatching {
            val courses = remoteDataSource.getCourses()
            (courses.inProgressCourses + courses.upcomingCourses)
                .sortedWith(compareBy({ it.courseDate }, { it.courseId }))
                .map { course -> course.toDomain(regionName = null) }
        }

    override suspend fun getCourseDetail(courseId: String): Result<CourseDetail> =
        runCatching {
            loadCourseDetail(courseId.toCourseIdLong())
        }

    override suspend fun searchPlaces(
        courseId: String,
        query: String,
    ): Result<List<CoursePlaceSearchResult>> =
        runCatching {
            val detail = loadCourseDetail(courseId.toCourseIdLong())
            val recommendedPlaceIdsByCurrentMember = detail.recommendedPlaces
                .filter { recommendation ->
                    val recommender = recommendation.recommender as? PlaceRecommender.Member
                    recommender?.memberId == detail.currentMemberId
                }
                .mapTo(mutableSetOf()) { recommendation -> recommendation.place.id }
            val coursePlaceIds = detail.coursePlaces.mapTo(mutableSetOf(), CoursePlace::id)

            remoteDataSource.searchPlaces(query).places.map { place ->
                val placeId = place.placeId?.toString()
                place.toDomain(
                    recommendedByCurrentMember = placeId != null &&
                        placeId in recommendedPlaceIdsByCurrentMember,
                    isInCourse = placeId != null && placeId in coursePlaceIds,
                )
            }
        }

    override suspend fun togglePlaceLike(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val recommendation = findRecommendationByPlaceId(courseIdLong, placeId)

            if (!recommendation.isLiked) {
                remoteDataSource.likeRecommendation(recommendation.recommendationId)
            }

            loadCourseDetail(courseIdLong)
        }

    override suspend fun recommendPlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            remoteDataSource.recommendPlace(
                courseId = courseIdLong,
                request = placeId.toPlaceIdLong().toMapRecommendationDto(),
            )
            loadCourseDetail(courseIdLong)
        }

    override suspend fun toggleCoursePlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
            val currentPlaceIds = detail.coursePlaces.map(CoursePlace::id)

            selectedCoursePlaceIdsByCourseId[courseIdLong] = if (placeId in currentPlaceIds) {
                currentPlaceIds - placeId
            } else {
                currentPlaceIds + placeId
            }

            loadCourseDetail(courseIdLong)
        }

    override suspend fun removeCoursePlace(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
            selectedCoursePlaceIdsByCourseId[courseIdLong] = detail.coursePlaces
                .map(CoursePlace::id)
                .filterNot { it == placeId }
            loadCourseDetail(courseIdLong)
        }

    override suspend fun moveCoursePlace(
        courseId: String,
        fromIndex: Int,
        toIndex: Int,
    ): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
            if (fromIndex !in detail.coursePlaces.indices || toIndex !in detail.coursePlaces.indices) {
                return@runCatching detail
            }

            selectedCoursePlaceIdsByCourseId[courseIdLong] = detail.coursePlaces
                .map(CoursePlace::id)
                .toMutableList()
                .apply {
                    val movedPlaceId = removeAt(fromIndex)
                    add(toIndex, movedPlaceId)
                }
            loadCourseDetail(courseIdLong)
        }

    override suspend fun updateCourseSettings(request: CourseUpdateRequest): Result<CourseDetail> =
        runCatching {
            val courseIdLong = request.courseId.toCourseIdLong()
            val currentDetail = loadCourseDetail(courseIdLong)
            val regionId = requireRegionId(request.region)

            remoteDataSource.updateCourseSetting(
                courseId = courseIdLong,
                request = request.toDto(
                    regionId = regionId,
                    relationship = currentDetail.relationship,
                ),
            )

            selectedCoursePlaceIdsByCourseId.remove(courseIdLong)
            loadCourseDetail(courseIdLong)
        }

    override suspend fun getPlaceComments(
        courseId: String,
        placeId: String,
    ): Result<CourseCommentThread> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
            CourseCommentThread(
                place = detail.findPlace(placeId),
                comments = commentsByCoursePlaceKey[CoursePlaceKey(courseIdLong, placeId)].orEmpty(),
            )
        }

    override suspend fun addPlaceComment(
        courseId: String,
        placeId: String,
        content: String,
    ): Result<CourseCommentThread> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
            val recommendation = findRecommendationByPlaceId(courseIdLong, placeId)
            val author = detail.currentMember ?: AnonymousMember
            val comment = remoteDataSource.addRecommendationComment(
                recommendationId = recommendation.recommendationId,
                request = content.toCommentDto(),
            ).toComment(
                author = author,
                content = content,
            )
            val key = CoursePlaceKey(courseIdLong, placeId)
            commentsByCoursePlaceKey[key] = commentsByCoursePlaceKey[key].orEmpty() + comment

            CourseCommentThread(
                place = detail.findPlace(placeId),
                comments = commentsByCoursePlaceKey[key].orEmpty(),
            )
        }

    private suspend fun loadCourseDetail(courseId: Long): CourseDetail {
        val courses = remoteDataSource.getCourses()
        val summaryCard = (courses.inProgressCourses + courses.upcomingCourses)
            .firstOrNull { it.courseId == courseId }
        val summaryCreated = courses.createdCourses.firstOrNull { it.courseId == courseId }
        val members = remoteDataSource.getCourseMembers(courseId).map { it.toDomain() }
        val currentMemberId = members.firstOrNull()?.id ?: AnonymousMember.id
        val recommendations = remoteDataSource.getCourseRecommendations(courseId)
        val recommendationPlacesById = recommendations.associate { recommendation ->
            recommendation.toPlace().id to recommendation.toPlace()
        }
        val serverCoursePlaces = remoteDataSource.getCoursePlaces(courseId)
            .sortedBy { it.placeOrder }
            .map { it.toDomain(recommendationPlacesById) }
        val selectedPlaceIds = selectedCoursePlaceIdsByCourseId.getOrPut(courseId) {
            val selectedRecommendationPlaceIds = recommendations
                .filter(CourseRecommendationDto::isSelected)
                .map { it.toPlace().id }

            serverCoursePlaces.map(CoursePlace::id)
                .ifEmpty { selectedRecommendationPlaceIds }
        }
        val placesById = (serverCoursePlaces + recommendationPlacesById.values)
            .associateBy(CoursePlace::id)
        val coursePlaces = selectedPlaceIds.mapNotNull(placesById::get)
        val relationship = (summaryCard?.participantType ?: summaryCreated?.participantType)
            .toRelationshipOrDefault()
        val courseName = summaryCard?.courseName ?: summaryCreated?.courseName
        val courseDate = summaryCard?.courseDate ?: summaryCreated?.courseDate

        return CourseDetail(
            id = courseId.toString(),
            name = courseName.orEmpty(),
            region = "",
            regionCoordinate = coursePlaces.firstOrNull()?.coordinate ?: DefaultCoordinate,
            date = courseDate?.toCourseDateOrDefault() ?: DefaultCourseDate,
            minBudget = 0,
            maxBudget = 0,
            relationship = relationship,
            currentMemberId = currentMemberId,
            members = members.ifEmpty { listOf(AnonymousMember) },
            recommendedPlaces = recommendations.map { recommendation ->
                recommendation.toDomain(currentMemberId)
            },
            coursePlaces = coursePlaces,
        )
    }

    private suspend fun findRecommendationByPlaceId(
        courseId: Long,
        placeId: String,
    ): CourseRecommendationDto =
        remoteDataSource.getCourseRecommendations(courseId)
            .firstOrNull { it.toPlace().id == placeId }
            ?: throw NoSuchElementException("Recommendation not found for placeId=$placeId")

    private fun CourseDetail.findPlace(placeId: String): CoursePlace =
        coursePlaces.firstOrNull { it.id == placeId }
            ?: recommendedPlaces.firstOrNull { it.place.id == placeId }?.place
            ?: throw NoSuchElementException("Place not found for placeId=$placeId")

    private fun requireRegionId(region: String): Long =
        localCourseRegionDataSource.getRegionId(region)
            ?: throw UnknownCourseRegionException(region)

    private fun Throwable.toCourseCreateException(): Throwable =
        if (this is CourseException) this else CourseRoomCreateException(this)

    private fun String?.toRelationshipOrDefault(): Relationship =
        when (orEmpty().trim().uppercase()) {
            "COUPLE",
            "LOVER",
            "TWO",
            -> Relationship.LOVER
            "FAMILY" -> Relationship.FAMILY
            else -> Relationship.FRIEND
        }

    private fun String.toCourseDateOrDefault(): CourseDate =
        runCatching {
            val values = split("-")
            CourseDate(
                year = values[0].toInt(),
                month = values[1].toInt(),
                day = values[2].toInt(),
            )
        }.getOrDefault(DefaultCourseDate)

    private data class CoursePlaceKey(
        val courseId: Long,
        val placeId: String,
    )

    private companion object {
        val DefaultCoordinate = CourseCoordinate(latitude = 37.5665, longitude = 126.9780)
        val DefaultCourseDate = CourseDate(year = 1970, month = 1, day = 1)
        val AnonymousMember = CourseMember(id = "member-current", name = "")
    }
}
