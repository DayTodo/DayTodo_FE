package com.team_daytodo.daytodo.data.dto.today

import kotlinx.serialization.Serializable

@Serializable
data class GetTodayCourseResponse(
    val todayCourse: TodayCourseDto?,
)

@Serializable
data class TodayCourseDto(
    val courseId: Long,
    val courseName: String,
    val members: List<TodayCourseMemberDto>,
    val places: List<TodayCoursePlaceDto>,
)

@Serializable
data class TodayCourseMemberDto(
    val nickname: String,
    val profileImageUrl: String? = null,
)

@Serializable
data class TodayCoursePlaceDto(
    val coursePlaceId: Long,
    val placeOrder: Int,
    val placeName: String,
    val category: String,
)
