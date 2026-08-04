package com.team_daytodo.daytodo.data.dto.today

import com.team_daytodo.daytodo.domain.today.model.TodayCourse
import com.team_daytodo.daytodo.domain.today.model.TodayCourseMember
import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
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

fun GetTodayCourseResponse.toDomain(): TodayCourse? = todayCourse?.toDomain()

fun TodayCourseDto.toDomain(): TodayCourse = TodayCourse(
    courseId = courseId,
    courseName = courseName,
    members = members.map { it.toDomain() },
    places = places.map { it.toDomain() },
)

fun TodayCourseMemberDto.toDomain(): TodayCourseMember = TodayCourseMember(
    nickname = nickname,
    profileImageUrl = profileImageUrl,
)

fun TodayCoursePlaceDto.toDomain(): TodayCoursePlace = TodayCoursePlace(
    coursePlaceId = coursePlaceId,
    placeOrder = placeOrder,
    placeName = placeName,
    category = category,
)
