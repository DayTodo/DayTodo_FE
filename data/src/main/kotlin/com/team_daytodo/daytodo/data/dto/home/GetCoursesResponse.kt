package com.team_daytodo.daytodo.data.dto.home

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.home.model.HomeBannerType
import com.team_daytodo.daytodo.domain.home.model.HomeCourseBanner
import com.team_daytodo.daytodo.domain.home.model.HomeCourseMember
import com.team_daytodo.daytodo.domain.home.model.HomeCourses
import com.team_daytodo.daytodo.domain.home.model.HomeTodayCourse
import com.team_daytodo.daytodo.domain.home.model.HomeUpcomingCourse
import java.time.LocalDate
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data class GetCoursesResponse(
    val banner: HomeCourseBannerDto,
    val todayCourse: HomeTodayCourseDto? = null,
    val upcomingCourses: List<HomeUpcomingCourseDto> = emptyList(),
)

@Serializable
data class HomeCourseBannerDto(
    val nickname: String,
    val type: String,
    val courseName: String? = null,
)

@Serializable
data class HomeTodayCourseDto(
    val courseId: Long,
    val courseName: String,
    val courseDate: String,
    val relationType: String,
    val members: List<HomeCourseMemberDto> = emptyList(),
)

@Serializable
data class HomeCourseMemberDto(
    val nickname: String,
    val profileImageUrl: String? = null,
)

@Serializable
data class HomeUpcomingCourseDto(
    val courseId: Long,
    val courseName: String,
    val courseDate: String,
    val participantType: String,
    val memberCount: Int,
    val dDay: Int,
)

fun GetCoursesResponse.toDomain(): HomeCourses = HomeCourses(
    banner = banner.toDomain(),
    todayCourse = todayCourse?.toDomain(),
    upcomingCourses = upcomingCourses.map { it.toDomain() },
)

private fun HomeCourseBannerDto.toDomain(): HomeCourseBanner = HomeCourseBanner(
    nickname = nickname,
    type = type.toHomeBannerType(),
    courseName = courseName,
)

private fun HomeTodayCourseDto.toDomain(): HomeTodayCourse = HomeTodayCourse(
    courseId = courseId,
    courseName = courseName,
    courseDate = LocalDate.parse(courseDate),
    relationship = relationType.toRelationship(),
    members = members.map { it.toDomain() },
)

private fun HomeCourseMemberDto.toDomain(): HomeCourseMember = HomeCourseMember(
    nickname = nickname,
    profileImageUrl = profileImageUrl,
)

private fun HomeUpcomingCourseDto.toDomain(): HomeUpcomingCourse = HomeUpcomingCourse(
    courseId = courseId,
    courseName = courseName,
    courseDate = LocalDate.parse(courseDate),
    relationship = participantType.toRelationship(),
    memberCount = memberCount,
    dDay = dDay,
)

private fun String.toHomeBannerType(): HomeBannerType =
    runCatching { HomeBannerType.valueOf(trim().uppercase()) }
        .getOrElse {
            Timber.w("Unknown HomeBannerType '%s', using NONE", this)
            HomeBannerType.NONE
        }

private fun String.toRelationship(): Relationship =
    when (trim().uppercase()) {
        "COUPLE", "LOVER" -> Relationship.LOVER
        "FRIEND" -> Relationship.FRIEND
        "FAMILY" -> Relationship.FAMILY
        else -> {
            Timber.w("Unknown RelationType '%s', using FRIEND", this)
            Relationship.FRIEND
        }
    }
