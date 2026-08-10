package com.team_daytodo.daytodo.domain.home.model

import com.team_daytodo.daytodo.core.model.Relationship
import java.time.LocalDate

data class HomeDashboard(
    val courses: HomeCourses,
    val magazines: List<HomeMagazine>,
)

data class HomeCourses(
    val banner: HomeCourseBanner,
    val todayCourse: HomeTodayCourse?,
    val upcomingCourses: List<HomeUpcomingCourse>,
)

data class HomeCourseBanner(
    val nickname: String,
    val type: HomeBannerType,
    val courseName: String?,
)

enum class HomeBannerType {
    ONGOING,
    DDAY,
    NONE,
}

data class HomeTodayCourse(
    val courseId: Long,
    val courseName: String,
    val courseDate: LocalDate,
    val relationship: Relationship,
    val members: List<HomeCourseMember>,
)

data class HomeCourseMember(
    val nickname: String,
    val profileImageUrl: String?,
)

data class HomeUpcomingCourse(
    val courseId: Long,
    val courseName: String,
    val courseDate: LocalDate,
    val relationship: Relationship,
    val memberCount: Int,
    val dDay: Int,
)

data class HomeMagazine(
    val magazineId: Long,
    val thumbnailUrl: String?,
    val placeName: String,
    val regionName: String,
    val tagline: String,
    val isAd: Boolean,
)
