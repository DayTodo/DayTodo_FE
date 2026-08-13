package com.team_daytodo.daytodo.data.course.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseCreateRequestDto(
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
data class CourseJoinRequestDto(
    @SerialName("inviteCode")
    val inviteCode: String,
)

@Serializable
data class CourseSettingRequestDto(
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
data class PlaceRecommendationRequestDto(
    @SerialName("source")
    val source: String,
    @SerialName("placeId")
    val placeId: Long,
)

@Serializable
data class PlaceRecommendationCommentRequestDto(
    @SerialName("content")
    val content: String,
)

@Serializable
data class AddCoursePlaceRequestDto(
    @SerialName("recommendationId")
    val recommendationId: Long,
)

@Serializable
data class ReorderCoursePlacesRequestDto(
    @SerialName("orderedCoursePlaceIds")
    val orderedCoursePlaceIds: List<Long>,
)

@Serializable
data class AiCourseRecommendationsRequestDto(
    @SerialName("regionId")
    val regionId: Long,
    @SerialName("minPrice")
    val minPrice: Int,
    @SerialName("maxPrice")
    val maxPrice: Int,
)
