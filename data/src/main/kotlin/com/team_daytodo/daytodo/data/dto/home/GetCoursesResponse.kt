package com.team_daytodo.daytodo.data.dto.home

import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.domain.home.model.HomeBannerStatus
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
    val status: String? = null,
    val message: String? = null,
    val courseId: Long? = null,
    val nickname: String? = null,
    val type: String? = null,
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

private fun HomeCourseBannerDto.toDomain(): HomeCourseBanner {
    val bannerStatus = (status ?: type.orEmpty()).toHomeBannerStatus()

    return HomeCourseBanner(
        status = bannerStatus,
        message = message.orEmpty(),
        courseId = courseId,
        nickname = nickname,
    )
}

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

private fun String.toHomeBannerStatus(): HomeBannerStatus =
    when (trim().uppercase()) {
        "ONGOING", "ON_GOING" -> HomeBannerStatus.ONGOING
        "DDAY", "D_DAY" -> HomeBannerStatus.DDAY
        "NONE", "EMPTY", "NO_COURSE" -> HomeBannerStatus.NONE
        else -> {
            Timber.w("Unknown HomeBannerStatus '%s', using UNKNOWN", this)
            HomeBannerStatus.UNKNOWN
        }
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
