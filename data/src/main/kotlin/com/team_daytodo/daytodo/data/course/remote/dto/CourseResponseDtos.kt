package com.team_daytodo.daytodo.data.course.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoursesResponseDto(
    @SerialName("courses")
    val courses: List<CourseSummaryDto> = emptyList(),
)

@Serializable
data class CourseSummaryDto(
    @SerialName("courseId")
    val courseId: Long,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("courseDate")
    val courseDate: String,
    @SerialName("memberCount")
    val memberCount: Int = 0,
    @SerialName("placeCount")
    val placeCount: Int = 0,
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
    val courseName: String,
    @SerialName("region")
    val region: CourseRegionDto? = null,
    @SerialName("courseDate")
    val courseDate: String,
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
    val role: String,
    @SerialName("joinedAt")
    val joinedAt: String,
)

@Serializable
data class CourseMembersResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: List<CourseMemberDto> = emptyList(),
)

@Serializable
data class CourseMemberDto(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("memberRole")
    val memberRole: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)

@Serializable
data class RemoveCourseMemberResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: String? = null,
)

@Serializable
data class CoursePlacesResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: List<CoursePlaceDto> = emptyList(),
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

@Serializable
data class CourseRecommendationsResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: List<CourseRecommendationDto> = emptyList(),
)

@Serializable
data class CourseRecommendationDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("source")
    val source: String,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("category")
    val category: String,
    @SerialName("minPrice")
    val minPrice: Int? = null,
    @SerialName("maxPrice")
    val maxPrice: Int? = null,
    @SerialName("address")
    val address: String,
    @SerialName("description")
    val description: String,
    @SerialName("isOpen")
    val isOpen: Boolean? = null,
    @SerialName("recommender")
    val recommender: String,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("commentCount")
    val commentCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("isSelected")
    val isSelected: Boolean,
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
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: PlaceRecommendationResultDto,
)

@Serializable
data class PlaceRecommendationResultDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
)

@Serializable
data class PlaceRecommendationLikeResponseDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: PlaceRecommendationLikeResultDto,
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
    @SerialName("success")
    val success: Boolean,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: PlaceRecommendationCommentResultDto,
)

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
