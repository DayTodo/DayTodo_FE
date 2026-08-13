package com.team_daytodo.daytodo.domain.course.model

import com.team_daytodo.daytodo.core.model.Relationship

data class CourseMember(
    val id: String,
    val name: String,
    val profileImageUrl: String? = null,
)

data class CourseCoordinate(
    val latitude: Double,
    val longitude: Double,
)

data class CourseSummary(
    val id: String,
    val name: String,
    val region: String,
    val date: CourseDate,
    val relationship: Relationship,
    val members: List<CourseMember>,
    val placeCount: Int,
)

data class CourseDetail(
    val id: String,
    val name: String,
    val region: String,
    val regionCoordinate: CourseCoordinate,
    val date: CourseDate,
    val minBudget: Int,
    val maxBudget: Int,
    val relationship: Relationship,
    val currentMemberId: String,
    val members: List<CourseMember>,
    val recommendedPlaces: List<CoursePlaceRecommendation>,
    val coursePlaces: List<CoursePlace>,
) {
    val currentMember: CourseMember?
        get() = members.firstOrNull { it.id == currentMemberId }
}

data class CoursePlace(
    val id: String,
    val coursePlaceId: String? = null,
    val recommendationId: String? = null,
    val name: String,
    val address: String,
    val category: String,
    val description: String,
    val expectedPrice: Int,
    val imageUrl: String? = null,
    val coordinate: CourseCoordinate,
)

data class CoursePlaceRecommendation(
    val recommendationId: String,
    val place: CoursePlace,
    val recommender: PlaceRecommender,
    val likedByMemberIds: Set<String>,
    val commentCount: Int,
) {
    fun isLikedBy(memberId: String): Boolean = memberId in likedByMemberIds
}

sealed interface PlaceRecommender {
    data object Ai : PlaceRecommender

    data class Member(
        val memberId: String,
        val name: String,
    ) : PlaceRecommender
}

data class CoursePlaceSearchResult(
    val place: CoursePlace,
    val recommendedByCurrentMember: Boolean,
    val isInCourse: Boolean,
)

data class CourseCommentThread(
    val place: CoursePlace,
    val comments: List<CourseComment>,
)

data class CourseComment(
    val id: String,
    val author: CourseMember,
    val content: String,
    val createdAtMillis: Long,
)

data class CourseUpdateRequest(
    val courseId: String,
    val name: String,
    val region: String,
    val date: CourseDate,
    val minBudget: Int,
    val maxBudget: Int,
)
