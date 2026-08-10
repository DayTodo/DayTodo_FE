package com.team_daytodo.daytodo.domain.today.model

data class TodayCourse(
    val courseId: Long,
    val courseName: String,
    val members: List<TodayCourseMember>,
    val places: List<TodayCoursePlace>,
)

data class TodayCourseMember(
    val nickname: String,
    val profileImageUrl: String?,
)

data class TodayCoursePlace(
    val coursePlaceId: Long,
    val placeOrder: Int,
    val placeName: String,
    val category: String,
)
