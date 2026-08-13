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
)

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
    val dDay: Long = 0,
    @SerialName("memberCount")
    val memberCount: Long = 0,
    @SerialName("participantType")
    val participantType: String? = null,
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
)

@Serializable
data class CourseCreateResponseDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("inviteCode")
    val inviteCode: String? = null,
    @SerialName("inviteCodeExpiredAt")
    val inviteCodeExpiredAt: String? = null,
)

@Serializable
data class CourseJoinResponseDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String,
)

@Serializable
data class CourseMemberDto(
    @SerialName("courseMemberId")
    val courseMemberId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("memberRole")
    val memberRole: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)

@Serializable
data class CoursePlaceDto(
    @SerialName("coursePlaceId")
    val coursePlaceId: Long,
    @SerialName("placeId")
    val placeId: Long,
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

// BE GET /courses/{courseId}/recommendations 는 recommendationId/source/placeName/좋아요·댓글수/
// isLiked/isSelected 만 내려주고 주소·카테고리·좌표·이미지 등 장소 상세 정보는 포함하지 않는다.
// (CourseResponse.Recommendation 확인) 아래 필드들은 BE가 보내지 않아 항상 비어있을 수 있다.
@Serializable
data class CourseRecommendationDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
    @SerialName("source")
    val source: String,
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

// BE POST /courses/{courseId}/recommendations 는 { recommendationId }만 flat하게 반환한다
// (CourseResponse.RecommendationCreated).
@Serializable
data class PlaceRecommendationResponseDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
)

// BE POST /courses/recommendations/{id}/likes 는 { recommendationLikeId, likeCount, isLiked }를
// flat하게 반환한다 (CourseResponse.RecommendationLike).
@Serializable
data class PlaceRecommendationLikeResponseDto(
    @SerialName("recommendationLikeId")
    val recommendationLikeId: Long,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
)

// BE POST /courses/recommendations/{id}/comments 는 { commentId }만 flat하게 반환한다
// (CourseResponse.RecommendationCommentCreated).
@Serializable
data class PlaceRecommendationCommentResponseDto(
    @SerialName("commentId")
    val commentId: Long,
)

@Serializable
data class PlacesSearchResponseDto(
    @SerialName("places")
    val places: List<PlaceSearchDto> = emptyList(),
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
    val category: String,
    @SerialName("regionName")
    val regionName: String,
    @SerialName("description")
    val description: String,
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
    val naverPlaceId: String,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("category")
    val category: String,
    @SerialName("address")
    val address: String,
    @SerialName("roadAddress")
    val roadAddress: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("description")
    val description: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("minPrice")
    val minPrice: Int,
    @SerialName("maxPrice")
    val maxPrice: Int,
    @SerialName("priceConfidence")
    val priceConfidence: Double,
    @SerialName("priceReason")
    val priceReason: String,
)
