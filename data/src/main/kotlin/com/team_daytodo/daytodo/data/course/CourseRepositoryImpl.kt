package com.team_daytodo.daytodo.data.course

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.data.course.mapper.successMessage
import com.team_daytodo.daytodo.data.course.mapper.toAddCoursePlaceDto
import com.team_daytodo.daytodo.data.course.mapper.toComment
import com.team_daytodo.daytodo.data.course.mapper.toCommentDto
import com.team_daytodo.daytodo.data.course.mapper.toCourseIdLong
import com.team_daytodo.daytodo.data.course.mapper.toDomain
import com.team_daytodo.daytodo.data.course.mapper.toDomainRecommendations
import com.team_daytodo.daytodo.data.course.mapper.toDto
import com.team_daytodo.daytodo.data.course.mapper.toDtoDate
import com.team_daytodo.daytodo.data.course.mapper.toJoinCourseDto
import com.team_daytodo.daytodo.data.course.mapper.toMapRecommendationDto
import com.team_daytodo.daytodo.data.course.mapper.toPlace
import com.team_daytodo.daytodo.data.course.mapper.toPlaceIdLong
import com.team_daytodo.daytodo.data.course.mapper.toReorderCoursePlacesDto
import com.team_daytodo.daytodo.data.course.remote.CourseRemoteDataSource
import com.team_daytodo.daytodo.data.course.remote.dto.AiCourseRecommendationsRequestDto
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
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceRecommendation
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

private const val DefaultAiRegionId = 1L
private const val DefaultAiMaxPrice = 1_000_000_000

class CourseRepositoryImpl @Inject constructor(
    private val remoteDataSource: CourseRemoteDataSource,
    private val localCourseRegionDataSource: LocalCourseRegionDataSource,
) : CourseRepository {
    private val aiRecommendationsByCourseId = mutableMapOf<Long, List<CoursePlaceRecommendation>>()
    private val commentsByCoursePlaceKey = mutableMapOf<CoursePlaceKey, List<CourseComment>>()
    private val recommendationIdsByCoursePlaceKey = mutableMapOf<CoursePlaceKey, Long>()
    private val transientPlacesByCoursePlaceKey = mutableMapOf<CoursePlaceKey, CoursePlace>()
    private val locallyUnlikedRecommendationIdsByCourseId = mutableMapOf<Long, Set<Long>>()
    private val currentMemberRecommendationIdsByCourseId = mutableMapOf<Long, Set<Long>>()

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

    override suspend fun getUpcomingCourses(
        startDate: CourseDate?,
        endDate: CourseDate?,
    ): Result<List<CourseSummary>> =
        runCatching {
            val response = remoteDataSource.getCourses(
                startDate = startDate?.toDtoDate(),
                endDate = endDate?.toDtoDate(),
            )
            val activeCourses = response.courseCards.map { course ->
                course.toDomain(
                    regionName = course.regionId?.let(::getRegionNameOrUnknown),
                )
            }
            val createdCourses = response.createdCourses.map { course ->
                course.toDomain(
                    regionName = course.regionId?.let(::getRegionNameOrUnknown),
                )
            }

            (activeCourses + createdCourses)
                .distinctBy(CourseSummary::id)
                .sortedWith(
                    compareBy<CourseSummary>(
                        { it.date.year },
                        { it.date.month },
                        { it.date.day },
                        { it.id.toLongOrNull() ?: Long.MAX_VALUE },
                    ),
                )
        }

    override suspend fun getCourseDetail(courseId: String): Result<CourseDetail> =
        runCatching {
            loadCourseDetail(courseId.toCourseIdLong())
        }

    override suspend fun refreshAiCourseRecommendations(courseId: String): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
            val regionId = detail.region
                .takeIf(String::isNotBlank)
                ?.let(localCourseRegionDataSource::getRegionId)
                ?: DefaultAiRegionId
            val minPrice = detail.minBudget.coerceAtLeast(0)
            val maxPrice = detail.maxBudget
                .takeIf { it > 0 && it >= minPrice }
                ?: DefaultAiMaxPrice
            val recommendations = remoteDataSource.getAiCourseRecommendations(
                AiCourseRecommendationsRequestDto(
                    regionId = regionId,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                ),
            ).toDomainRecommendations()

            aiRecommendationsByCourseId[courseIdLong] = recommendations
            recommendations.forEach { recommendation ->
                transientPlacesByCoursePlaceKey[CoursePlaceKey(courseIdLong, recommendation.place.id)] =
                    recommendation.place
            }
            loadCourseDetail(courseIdLong)
        }

    override suspend fun searchPlaces(
        courseId: String,
        query: String,
    ): Result<List<CoursePlaceSearchResult>> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val detail = loadCourseDetail(courseIdLong)
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
            }.also { results ->
                results.forEach { result ->
                    transientPlacesByCoursePlaceKey[CoursePlaceKey(courseIdLong, result.place.id)] =
                        result.place
                }
            }
        }

    override suspend fun togglePlaceLike(
        courseId: String,
        placeId: String,
    ): Result<CourseDetail> =
        runCatching {
            val courseIdLong = courseId.toCourseIdLong()
            val recommendation = findRecommendationByPlaceIdOrCreate(courseIdLong, placeId)
            val locallyUnlikedRecommendationIds = locallyUnlikedRecommendationIdsByCourseId[courseIdLong].orEmpty()

            if (recommendation.recommendationId in locallyUnlikedRecommendationIds) {
                locallyUnlikedRecommendationIdsByCourseId[courseIdLong] =
                    locallyUnlikedRecommendationIds - recommendation.recommendationId
            } else if (recommendation.isLiked) {
                locallyUnlikedRecommendationIdsByCourseId[courseIdLong] =
                    locallyUnlikedRecommendationIds + recommendation.recommendationId
            } else {
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
            findRecommendationByPlaceIdOrCreate(
                courseId = courseIdLong,
                placeId = placeId,
                markAsCurrentMemberRecommendation = true,
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
            val currentPlace = detail.findCoursePlaceByDisplayPlaceId(placeId)

            if (currentPlace != null) {
                val coursePlaceId = currentPlace.coursePlaceId?.toLongOrNull()
                if (coursePlaceId != null) {
                    remoteDataSource.removeCoursePlace(courseIdLong, coursePlaceId)
                }
            } else {
                val recommendation = findRecommendationByPlaceIdOrCreate(courseIdLong, placeId)
                remoteDataSource.addRecommendationToCourse(
                    courseId = courseIdLong,
                    request = recommendation.recommendationId.toAddCoursePlaceDto(),
                )
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
            val coursePlaceId = detail.findCoursePlaceByDisplayPlaceId(placeId)
                ?.coursePlaceId
                ?.toLongOrNull()

            if (coursePlaceId != null) {
                remoteDataSource.removeCoursePlace(courseIdLong, coursePlaceId)
            }
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

            val movedPlaces = detail.coursePlaces.move(fromIndex, toIndex)
            val orderedCoursePlaceIds = movedPlaces.mapNotNull { place ->
                place.coursePlaceId?.toLongOrNull()
            }

            if (orderedCoursePlaceIds.size == movedPlaces.size) {
                remoteDataSource.reorderCoursePlaces(
                    courseId = courseIdLong,
                    request = orderedCoursePlaceIds.toReorderCoursePlacesDto(),
                )
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
            val recommendation = findRecommendationByPlaceIdOrCreate(courseIdLong, placeId)
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
        val summaryCard = courses.courseCards.firstOrNull { it.courseId == courseId }
        val summaryCreated = courses.createdCourses.firstOrNull { it.courseId == courseId }
        val members = remoteDataSource.getCourseMembers(courseId).map { it.toDomain() }
        val currentMemberId = members.firstOrNull()?.id ?: AnonymousMember.id
        val recommendations = remoteDataSource.getCourseRecommendations(courseId)
        val recommendationPlacesById = buildMap {
            recommendations.forEach { recommendation ->
                val place = transientPlaceForRecommendation(courseId, recommendation)
                    ?.copy(recommendationId = recommendation.recommendationId.toString())
                    ?: recommendation.toPlace()
                put(place.id, place)
                put(place.name, place)
                put("recommendation-${recommendation.recommendationId}", place)
            }
        }
        val serverCoursePlaces = remoteDataSource.getCoursePlaces(courseId)
            .sortedBy { it.placeOrder }
            .map { it.toDomain(recommendationPlacesById) }
        val coursePlaces = serverCoursePlaces

        val relationship = (
            summaryCard?.relationType
                ?: summaryCard?.participantType
                ?: summaryCreated?.relationType
                ?: summaryCreated?.participantType
            ).toRelationshipOrDefault()
        val courseName = summaryCard?.courseName ?: summaryCreated?.courseName
        val courseDate = summaryCard?.courseDate ?: summaryCreated?.courseDate
        val region = summaryCard?.region?.regionName
            ?: summaryCard?.regionName
            ?: summaryCard?.regionId?.let(::getRegionNameOrUnknown)
            ?: summaryCreated?.region?.regionName
            ?: summaryCreated?.regionName
            ?: summaryCreated?.regionId?.let(::getRegionNameOrUnknown)
            ?: ""
        val minBudget = summaryCard?.minBudget
            ?: summaryCreated?.minBudget
            ?: summaryCard?.minPrice
            ?: summaryCreated?.minPrice
            ?: 0
        val maxBudget = summaryCard?.maxBudget
            ?: summaryCreated?.maxBudget
            ?: summaryCard?.maxPrice
            ?: summaryCreated?.maxPrice
            ?: 0

        val currentMember = members.firstOrNull { it.id == currentMemberId } ?: AnonymousMember
        val localUnlikedRecommendationIds = locallyUnlikedRecommendationIdsByCourseId[courseId].orEmpty()
        val currentMemberRecommendationIds = currentMemberRecommendationIdsByCourseId[courseId].orEmpty()
        val serverRecommendationDomains = recommendations.map { recommendation ->
            val domain = recommendation.toDomain(currentMemberId)
            val domainWithLocalRecommender = if (recommendation.recommendationId in currentMemberRecommendationIds) {
                domain.copy(
                    recommender = PlaceRecommender.Member(
                        memberId = currentMemberId,
                        name = currentMember.name.ifBlank { "나" },
                    ),
                )
            } else {
                domain
            }
            val domainWithLocalLikeState = if (recommendation.recommendationId in localUnlikedRecommendationIds) {
                domainWithLocalRecommender.copy(
                    likedByMemberIds = domainWithLocalRecommender.likedByMemberIds - currentMemberId,
                )
            } else {
                domainWithLocalRecommender
            }
            transientPlaceForRecommendation(courseId, recommendation)
                ?.let { transientPlace ->
                    domainWithLocalLikeState.copy(
                        place = transientPlace.copy(
                            recommendationId = recommendation.recommendationId.toString(),
                        ),
                    )
                }
                ?: domainWithLocalLikeState
        }
        val transientAiRecommendations = aiRecommendationsByCourseId[courseId].orEmpty()
            .filterNot { aiRecommendation ->
                serverRecommendationDomains.any { serverRecommendation ->
                    serverRecommendation.place.id == aiRecommendation.place.id ||
                        serverRecommendation.place.name == aiRecommendation.place.name
                }
            }
        val recommendationDomains = transientAiRecommendations + serverRecommendationDomains

        return CourseDetail(
            id = courseId.toString(),
            name = courseName.orEmpty(),
            region = region,
            regionCoordinate = coursePlaces.firstOrNull()?.coordinate
                ?: recommendationDomains.firstOrNull()?.place?.coordinate
                ?: DefaultCoordinate,
            date = courseDate?.toCourseDateOrDefault() ?: DefaultCourseDate,
            minBudget = minBudget,
            maxBudget = maxBudget,
            relationship = relationship,
            currentMemberId = currentMemberId,
            members = members.ifEmpty { listOf(AnonymousMember) },
            recommendedPlaces = recommendationDomains,
            coursePlaces = coursePlaces,
        )
    }

    private suspend fun findRecommendationByPlaceId(
        courseId: Long,
        placeId: String,
    ): CourseRecommendationDto =
        remoteDataSource.getCourseRecommendations(courseId)
            .findMatchingRecommendation(courseId, placeId)
            ?: throw NoSuchElementException("Recommendation not found for placeId=$placeId")

    private suspend fun findRecommendationByPlaceIdOrCreate(
        courseId: Long,
        placeId: String,
        markAsCurrentMemberRecommendation: Boolean = false,
    ): CourseRecommendationDto {
        remoteDataSource.getCourseRecommendations(courseId)
            .findMatchingRecommendation(courseId, placeId)
            ?.let { recommendation ->
                if (markAsCurrentMemberRecommendation) {
                    markCurrentMemberRecommendation(courseId, recommendation.recommendationId)
                }
                return recommendation
            }

        val placeIdLong = placeId.toPlaceIdLong()
        val created = remoteDataSource.recommendPlace(
            courseId = courseId,
            request = placeIdLong.toMapRecommendationDto(),
        )
        val recommendationId = created.resolvedRecommendationId
        recommendationIdsByCoursePlaceKey[CoursePlaceKey(courseId, placeId)] = recommendationId
        if (markAsCurrentMemberRecommendation) {
            markCurrentMemberRecommendation(courseId, recommendationId)
        }
        return remoteDataSource.getCourseRecommendations(courseId)
            .firstOrNull { it.recommendationId == recommendationId }
            ?: CourseRecommendationDto(
                recommendationId = recommendationId,
                placeId = placeIdLong,
                source = "MEMBER",
                placeName = transientPlacesByCoursePlaceKey[CoursePlaceKey(courseId, placeId)]?.name.orEmpty(),
            )
    }

    private fun markCurrentMemberRecommendation(
        courseId: Long,
        recommendationId: Long,
    ) {
        currentMemberRecommendationIdsByCourseId[courseId] =
            currentMemberRecommendationIdsByCourseId[courseId].orEmpty() + recommendationId
    }

    private fun List<CourseRecommendationDto>.findMatchingRecommendation(
        courseId: Long,
        targetPlaceId: String,
    ): CourseRecommendationDto? {
        val key = CoursePlaceKey(courseId, targetPlaceId)
        val cachedRecommendationId = recommendationIdsByCoursePlaceKey[key]
        if (cachedRecommendationId != null) {
            firstOrNull { it.recommendationId == cachedRecommendationId }?.let { return it }
        }

        val cachedPlace = transientPlacesByCoursePlaceKey[key]
        return firstOrNull { recommendation ->
            recommendation.matchesPlaceId(targetPlaceId) ||
                (cachedPlace != null && recommendation.placeName == cachedPlace.name)
        }?.also { recommendation ->
            recommendationIdsByCoursePlaceKey[key] = recommendation.recommendationId
        }
    }

    private fun transientPlaceForRecommendation(
        courseId: Long,
        recommendation: CourseRecommendationDto,
    ): CoursePlace? {
        recommendationIdsByCoursePlaceKey.entries
            .firstOrNull { (key, recommendationId) ->
                key.courseId == courseId && recommendationId == recommendation.recommendationId
            }
            ?.key
            ?.let(transientPlacesByCoursePlaceKey::get)
            ?.let { return it }

        return transientPlacesByCoursePlaceKey
            .filterKeys { it.courseId == courseId }
            .values
            .firstOrNull { it.name == recommendation.placeName }
    }

    private fun CourseRecommendationDto.matchesPlaceId(targetPlaceId: String): Boolean =
        placeId?.toString() == targetPlaceId ||
            "recommendation-$recommendationId" == targetPlaceId ||
            toPlace().id == targetPlaceId

    private fun List<CoursePlace>.move(
        fromIndex: Int,
        toIndex: Int,
    ): List<CoursePlace> =
        toMutableList().apply {
            val movedPlace = removeAt(fromIndex)
            add(toIndex, movedPlace)
        }

    private fun CourseDetail.findPlace(placeId: String): CoursePlace =
        coursePlaces.firstOrNull { it.id == placeId }
            ?: recommendedPlaces.firstOrNull { it.place.id == placeId }?.place
            ?: throw NoSuchElementException("Place not found for placeId=$placeId")

    private fun CourseDetail.findCoursePlaceByDisplayPlaceId(placeId: String): CoursePlace? {
        coursePlaces.firstOrNull { it.id == placeId }?.let { return it }
        val recommendationPlace = recommendedPlaces.firstOrNull { it.place.id == placeId }?.place
            ?: return null

        return coursePlaces.firstOrNull { it.name == recommendationPlace.name }
    }

    private fun getRegionNameOrUnknown(regionId: Long): String =
        localCourseRegionDataSource.getRegionName(regionId) ?: UnknownRegionName

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
        const val UnknownRegionName = "알 수 없는 지역"
    }
}
