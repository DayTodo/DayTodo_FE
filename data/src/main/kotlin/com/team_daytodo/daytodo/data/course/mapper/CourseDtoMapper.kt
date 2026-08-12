package com.team_daytodo.daytodo.data.course.mapper

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.data.course.remote.dto.AddCoursePlaceRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.AiCourseRecommendationsResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.AiRecommendedPlaceDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseCreateRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseCreateResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseJoinRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseJoinResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseMemberDto
import com.team_daytodo.daytodo.data.course.remote.dto.CoursePlaceDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseRecommendationDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseSettingRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.CourseSummaryDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationCommentRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationCommentResponseDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceRecommendationRequestDto
import com.team_daytodo.daytodo.data.course.remote.dto.PlaceSearchDto
import com.team_daytodo.daytodo.data.course.remote.dto.ReorderCoursePlacesRequestDto
import com.team_daytodo.daytodo.domain.course.model.CourseComment
import com.team_daytodo.daytodo.domain.course.model.CourseCoordinate
import com.team_daytodo.daytodo.domain.course.model.CourseCreateRequest
import com.team_daytodo.daytodo.domain.course.model.CourseCreateResult
import com.team_daytodo.daytodo.domain.course.model.CourseDate
import com.team_daytodo.daytodo.domain.course.model.CourseMember
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceRecommendation
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.domain.course.model.CourseSummary
import com.team_daytodo.daytodo.domain.course.model.CourseUpdateRequest
import com.team_daytodo.daytodo.domain.course.model.PlaceRecommender
import java.time.LocalDate

internal fun CourseCreateRequest.toDto(regionId: Long): CourseCreateRequestDto =
    CourseCreateRequestDto(
        courseName = roomName.trim(),
        regionId = regionId,
        courseDate = date.toDtoDate(),
        minPrice = minBudget,
        maxPrice = maxBudget,
        participantType = relationship.toParticipantTypeDto(),
    )

internal fun CourseUpdateRequest.toDto(
    regionId: Long,
    relationship: Relationship,
): CourseSettingRequestDto =
    CourseSettingRequestDto(
        courseName = name.trim(),
        regionId = regionId,
        courseDate = date.toDtoDate(),
        minPrice = minBudget,
        maxPrice = maxBudget,
        participantType = relationship.toParticipantTypeDto(),
    )

internal fun String.toJoinCourseDto(): CourseJoinRequestDto =
    CourseJoinRequestDto(inviteCode = trim())

internal fun Long.toMapRecommendationDto(): PlaceRecommendationRequestDto =
    PlaceRecommendationRequestDto(
        source = RecommendationSourceMember,
        placeId = this,
    )

internal fun Long.toAddCoursePlaceDto(): AddCoursePlaceRequestDto =
    AddCoursePlaceRequestDto(recommendationId = this)

internal fun List<Long>.toReorderCoursePlacesDto(): ReorderCoursePlacesRequestDto =
    ReorderCoursePlacesRequestDto(orderedCoursePlaceIds = this)

internal fun String.toCommentDto(): PlaceRecommendationCommentRequestDto =
    PlaceRecommendationCommentRequestDto(content = trim())

internal fun CourseCreateResponseDto.toDomain(): CourseCreateResult =
    CourseCreateResult(
        inviteLink = inviteLink
            ?: shareLink
            ?: inviteCode
            ?: "$DefaultInviteBaseUrl/$courseId",
    )

internal fun CourseSummaryDto.toDomain(regionName: String?): CourseSummary =
    CourseSummary(
        id = courseId.toString(),
        name = courseName,
        region = region?.regionName ?: this.regionName ?: regionName.orEmpty(),
        date = courseDate.toCourseDate(),
        relationship = (relationType ?: participantType).toRelationship(),
        members = List(memberCount.coerceAtLeast(0)) { index ->
            CourseMember(
                id = "member-count-${courseId}-${index + 1}",
                name = "",
            )
        },
        placeCount = placeCount,
    )

internal fun CourseJoinResponseDto.successMessage(): String =
    "$courseName joined"

internal fun CourseMemberDto.toDomain(): CourseMember =
    CourseMember(
        id = (userId ?: memberId ?: courseMemberId ?: 0L).toString(),
        name = nickname,
        profileImageUrl = profileImageUrl,
    )

internal fun CourseRecommendationDto.toPlace(): CoursePlace =
    CoursePlace(
        id = stablePlaceId(),
        recommendationId = recommendationId.toString(),
        name = placeName,
        address = roadAddress ?: address,
        category = category.ifBlank { DefaultCategory },
        description = description.ifBlank { placeName },
        expectedPrice = minPrice ?: maxPrice ?: 0,
        imageUrl = imageUrl,
        coordinate = CourseCoordinate(
            latitude = latitude ?: DefaultLatitude,
            longitude = longitude ?: DefaultLongitude,
        ),
    )

internal fun CoursePlaceDto.toDomain(
    recommendationPlacesById: Map<String, CoursePlace>,
): CoursePlace {
    val placeIdString = placeId?.toString() ?: coursePlaceId?.toString().orEmpty()
    val recommendationPlace = recommendationPlacesById[placeIdString] ?: recommendationPlacesById[placeName]

    return CoursePlace(
        id = placeIdString.ifBlank { stableCoursePlaceId() },
        coursePlaceId = coursePlaceId?.toString(),
        name = placeName,
        address = address ?: recommendationPlace?.address.orEmpty(),
        category = category ?: recommendationPlace?.category.orEmpty(),
        description = description ?: recommendationPlace?.description.orEmpty(),
        expectedPrice = recommendationPlace?.expectedPrice ?: 0,
        imageUrl = imageUrl ?: recommendationPlace?.imageUrl,
        coordinate = CourseCoordinate(
            latitude = latitude ?: recommendationPlace?.coordinate?.latitude ?: DefaultLatitude,
            longitude = longitude ?: recommendationPlace?.coordinate?.longitude ?: DefaultLongitude,
        ),
    )
}

internal fun CourseRecommendationDto.toDomain(currentMemberId: String): CoursePlaceRecommendation {
    val syntheticLikedMemberIds = buildSet {
        if (isLiked) add(currentMemberId)
        val remainingLikeCount = likeCount - size
        repeat(remainingLikeCount.coerceAtLeast(0)) { index ->
            add("liked-member-$recommendationId-$index")
        }
    }

    return CoursePlaceRecommendation(
        recommendationId = recommendationId.toString(),
        place = toPlace(),
        recommender = toRecommender(),
        likedByMemberIds = syntheticLikedMemberIds,
        commentCount = commentCount,
    )
}

internal fun AiCourseRecommendationsResponseDto.toDomainRecommendations(): List<CoursePlaceRecommendation> =
    result.flatMap { course ->
        course.places.map { place ->
            place.toDomainRecommendation(courseName = course.courseName)
        }
    }

private fun AiRecommendedPlaceDto.toDomainRecommendation(courseName: String): CoursePlaceRecommendation =
    CoursePlaceRecommendation(
        recommendationId = "",
        place = CoursePlace(
            id = placeId.toString(),
            name = placeName,
            address = roadAddress.ifBlank { address },
            category = category.ifBlank { courseName },
            description = description.ifBlank { courseName },
            expectedPrice = minPrice,
            imageUrl = imageUrl,
            coordinate = CourseCoordinate(
                latitude = latitude,
                longitude = longitude,
            ),
        ),
        recommender = PlaceRecommender.Ai,
        likedByMemberIds = emptySet(),
        commentCount = 0,
    )

internal fun PlaceSearchDto.toDomain(
    recommendedByCurrentMember: Boolean,
    isInCourse: Boolean,
): CoursePlaceSearchResult =
    CoursePlaceSearchResult(
        place = CoursePlace(
            id = placeId?.toString() ?: stableSearchPlaceId(),
            name = placeName,
            address = roadAddress ?: address ?: regionName,
            category = category,
            description = description,
            expectedPrice = 0,
            imageUrl = imageUrl,
            coordinate = CourseCoordinate(
                latitude = latitude ?: DefaultLatitude,
                longitude = longitude ?: DefaultLongitude,
            ),
        ),
        recommendedByCurrentMember = recommendedByCurrentMember,
        isInCourse = isInCourse,
    )

internal fun PlaceRecommendationCommentResponseDto.toComment(
    author: CourseMember,
    content: String,
): CourseComment =
    CourseComment(
        id = result.commentId.toString(),
        author = author,
        content = content.trim(),
        createdAtMillis = System.currentTimeMillis(),
    )

internal fun String.toCourseIdLong(): Long =
    trim().toLongOrNull()
        ?: throw IllegalArgumentException("Invalid courseId: $this")

internal fun String.toPlaceIdLong(): Long =
    trim().toLongOrNull()
        ?: throw IllegalArgumentException(
            "This place does not have a server placeId. Check the place search API response.",
        )

internal fun CourseDate.toDtoDate(): String =
    "%04d-%02d-%02d".format(year, month, day)

private fun CourseRecommendationDto.toRecommender(): PlaceRecommender =
    if (source.equals(RecommendationSourceAi, ignoreCase = true)) {
        PlaceRecommender.Ai
    } else {
        PlaceRecommender.Member(
            memberId = "member-${recommender.hashCode().toUInt()}",
            name = recommender,
        )
    }

private fun String?.toRelationship(): Relationship =
    when (orEmpty().trim().uppercase()) {
        "COUPLE",
        "LOVER",
        "TWO",
        -> Relationship.LOVER
        "FAMILY" -> Relationship.FAMILY
        else -> Relationship.FRIEND
    }

private fun Relationship.toParticipantTypeDto(): String =
    when (this) {
        Relationship.LOVER -> "TWO"
        Relationship.FRIEND -> "FRIEND"
        Relationship.FAMILY -> "FAMILY"
    }

private fun String.toCourseDate(): CourseDate {
    val localDate = LocalDate.parse(this)
    return CourseDate(
        year = localDate.year,
        month = localDate.monthValue,
        day = localDate.dayOfMonth,
    )
}

private fun PlaceSearchDto.stableSearchPlaceId(): String =
    naverPlaceId?.takeIf(String::isNotBlank)
        ?: "search-${"$placeName|$regionName|$category".hashCode().toUInt()}"

private fun CourseRecommendationDto.stablePlaceId(): String =
    placeId?.toString() ?: "recommendation-$recommendationId"

private fun CoursePlaceDto.stableCoursePlaceId(): String =
    "course-place-${coursePlaceId ?: "$placeName|$placeOrder".hashCode().toUInt()}"

private const val RecommendationSourceAi = "AI"
private const val RecommendationSourceMember = "MEMBER"
private const val DefaultInviteBaseUrl = "https://daytodo.app/courses/join"
private const val DefaultCategory = "Place"
private const val DefaultLatitude = 37.5665
private const val DefaultLongitude = 126.9780
