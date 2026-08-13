package com.team_daytodo.daytodo.data.course.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoursesResponseDto(
    @SerialName("banner")
    val banner: CourseBannerDto? = null,
    @SerialName("inProgressCourses")
    val inProgressCourses: List<CourseCardDto> = emptyList(),
    @SerialName("upcomingCourses")
    val upcomingCourses: List<CourseCardDto> = emptyList(),
    @SerialName("createdCourses")
    val createdCourses: List<CreatedCourseDto> = emptyList(),
    @SerialName("courses")
    val legacyCourses: List<CourseCardDto> = emptyList(),
) {
    val courseCards: List<CourseCardDto>
        get() = (inProgressCourses + upcomingCourses + legacyCourses)
            .distinctBy(CourseCardDto::courseId)
}

@Serializable
data class CourseBannerDto(
    @SerialName("status")
    val status: String? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("courseId")
    val courseId: Long? = null,
)

@Serializable
data class CourseCardDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("courseDate")
    val courseDate: String,
    @SerialName("dDay")
    val dDay: Long? = null,
    @SerialName("memberCount")
    val memberCount: Long = 0,
    @SerialName("placeCount")
    val placeCount: Long = 0,
    @SerialName("participantType")
    val participantType: String? = null,
    @SerialName("minPrice")
    val minPrice: Int? = null,
    @SerialName("maxPrice")
    val maxPrice: Int? = null,
    @SerialName("minBudget")
    val minBudget: Int? = null,
    @SerialName("maxBudget")
    val maxBudget: Int? = null,
    @SerialName("relationType")
    val relationType: String? = null,
    @SerialName("frameColor")
    val frameColor: String? = null,
    @SerialName("region")
    val region: CourseRegionDto? = null,
    @SerialName("regionId")
    val regionId: Long? = null,
    @SerialName("regionName")
    val regionName: String? = null,
)

@Serializable
data class CreatedCourseDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("courseDate")
    val courseDate: String,
    @SerialName("memberCount")
    val memberCount: Long = 0,
    @SerialName("placeCount")
    val placeCount: Long = 0,
    @SerialName("participantType")
    val participantType: String? = null,
    @SerialName("minPrice")
    val minPrice: Int? = null,
    @SerialName("maxPrice")
    val maxPrice: Int? = null,
    @SerialName("minBudget")
    val minBudget: Int? = null,
    @SerialName("maxBudget")
    val maxBudget: Int? = null,
    @SerialName("relationType")
    val relationType: String? = null,
    @SerialName("region")
    val region: CourseRegionDto? = null,
    @SerialName("regionId")
    val regionId: Long? = null,
    @SerialName("regionName")
    val regionName: String? = null,
)

@Serializable
data class CourseRegionDto(
    @SerialName("regionId")
    val regionId: Long,
    @SerialName("regionName")
    val regionName: String,
)

@Serializable
data class CourseCreateResponseDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String? = null,
    @SerialName("region")
    val region: CourseRegionDto? = null,
    @SerialName("courseDate")
    val courseDate: String? = null,
    @SerialName("minBudget")
    val minBudget: Int? = null,
    @SerialName("maxBudget")
    val maxBudget: Int? = null,
    @SerialName("minPrice")
    val minPrice: Int? = null,
    @SerialName("maxPrice")
    val maxPrice: Int? = null,
    @SerialName("memberType")
    val memberType: String? = null,
    @SerialName("participantType")
    val participantType: String? = null,
    @SerialName("inviteCode")
    val inviteCode: String? = null,
    @SerialName("inviteCodeExpiredAt")
    val inviteCodeExpiredAt: String? = null,
    @SerialName("inviteLink")
    val inviteLink: String? = null,
    @SerialName("shareLink")
    val shareLink: String? = null,
)

@Serializable
data class CourseJoinResponseDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("role")
    val role: String? = null,
    @SerialName("joinedAt")
    val joinedAt: String? = null,
)

@Serializable
data class CourseMemberDto(
    @SerialName("memberId")
    val memberId: Long? = null,
    @SerialName("courseMemberId")
    val courseMemberId: Long? = null,
    @SerialName("userId")
    val userId: Long? = null,
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("memberRole")
    val memberRole: String? = null,
    @SerialName("memberStatus")
    val memberStatus: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)

@Serializable
data class CoursePlaceDto(
    @SerialName("coursePlaceId")
    val coursePlaceId: Long? = null,
    @SerialName("placeId")
    val placeId: Long? = null,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("placeOrder")
    val placeOrder: Int,
    @SerialName("category")
    val category: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
)

@Serializable
data class CourseSettingResponseDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("regionId")
    val regionId: Long,
    @SerialName("courseDate")
    val courseDate: String,
    @SerialName("minPrice")
    val minPrice: Int,
    @SerialName("maxPrice")
    val maxPrice: Int,
    @SerialName("participantType")
    val participantType: String,
)

// BE GET /courses/{courseId}/recommendations returns sparse place data.
@Serializable
data class CourseRecommendationDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
    @SerialName("source")
    val source: String = "",
    @SerialName("placeName")
    val placeName: String,
    @SerialName("likeCount")
    val likeCount: Int = 0,
    @SerialName("commentCount")
    val commentCount: Int = 0,
    @SerialName("isLiked")
    val isLiked: Boolean = false,
    @SerialName("isSelected")
    val isSelected: Boolean = false,
    @SerialName("placeId")
    val placeId: Long? = null,
    @SerialName("category")
    val category: String? = null,
    @SerialName("minPrice")
    val minPrice: Int? = null,
    @SerialName("maxPrice")
    val maxPrice: Int? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("isOpen")
    val isOpen: Boolean? = null,
    @SerialName("recommender")
    val recommender: String? = null,
    @SerialName("roadAddress")
    val roadAddress: String? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
)

@Serializable
data class PlaceRecommendationResponseDto(
    @SerialName("recommendationId")
    val recommendationId: Long? = null,
    @SerialName("result")
    val result: PlaceRecommendationResultDto? = null,
) {
    val resolvedRecommendationId: Long
        get() = recommendationId ?: result?.recommendationId
            ?: error("Missing recommendationId in recommendation response")
}

@Serializable
data class PlaceRecommendationResultDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
)

@Serializable
data class PlaceRecommendationLikeResponseDto(
    @SerialName("recommendationLikeId")
    val recommendationLikeId: Long? = null,
    @SerialName("likeCount")
    val likeCount: Int? = null,
    @SerialName("isLiked")
    val isLiked: Boolean? = null,
    @SerialName("result")
    val result: PlaceRecommendationLikeResultDto? = null,
)

@Serializable
data class PlaceRecommendationLikeResultDto(
    @SerialName("recommendationLikeId")
    val recommendationLikeId: Long,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
)

@Serializable
data class PlaceRecommendationCommentResponseDto(
    @SerialName("commentId")
    val commentId: Long? = null,
    @SerialName("result")
    val result: PlaceRecommendationCommentResultDto? = null,
) {
    val resolvedCommentId: Long
        get() = commentId ?: result?.commentId
            ?: error("Missing commentId in recommendation comment response")
}

@Serializable
data class PlaceRecommendationCommentResultDto(
    @SerialName("commentId")
    val commentId: Long,
)

@Serializable
data class PlacesSearchResponseDto(
    @SerialName("places")
    val places: List<PlaceSearchDto> = emptyList(),
)

@Serializable
data class CoursePlaceAddedResponseDto(
    @SerialName("coursePlaceId")
    val coursePlaceId: Long,
)

@Serializable
data class CoursePlacesBodyDto(
    @SerialName("places")
    val places: List<CoursePlaceDto> = emptyList(),
)

@Serializable
data class PlaceSearchDto(
    @SerialName("placeId")
    val placeId: Long? = null,
    @SerialName("naverPlaceId")
    val naverPlaceId: String? = null,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("category")
    val category: String? = null,
    @SerialName("regionName")
    val regionName: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("roadAddress")
    val roadAddress: String? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
)

@Serializable
data class AiCourseRecommendationsResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: List<AiRecommendedCourseDto> = emptyList(),
)

@Serializable
data class AiRecommendedCourseDto(
    @SerialName("courseName")
    val courseName: String,
    @SerialName("estimatedTotalMinPrice")
    val estimatedTotalMinPrice: Int,
    @SerialName("estimatedTotalMaxPrice")
    val estimatedTotalMaxPrice: Int,
    @SerialName("places")
    val places: List<AiRecommendedPlaceDto> = emptyList(),
)

@Serializable
data class AiRecommendedPlaceDto(
    @SerialName("recommendationOrder")
    val recommendationOrder: Int,
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("naverPlaceId")
    val naverPlaceId: String? = null,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("category")
    val category: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("roadAddress")
    val roadAddress: String? = null,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("description")
    val description: String? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("minPrice")
    val minPrice: Int = 0,
    @SerialName("maxPrice")
    val maxPrice: Int = 0,
    @SerialName("priceConfidence")
    val priceConfidence: Double? = null,
    @SerialName("priceReason")
    val priceReason: String? = null,
)
