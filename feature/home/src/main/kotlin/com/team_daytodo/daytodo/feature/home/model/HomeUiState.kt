package com.team_daytodo.daytodo.feature.home.model

data class HomeUiState(
    val username: String = "",
    val interestLocation: String = "",
    val todayCourse: TodayCourse? = null,
    val upcomingCourse: UpcomingCourse? = null,
    val createdCourses: List<CreatedCourse> = emptyList(),
    val todayPickMagazines: List<HomeMagazineUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasTodaySchedule: Boolean
        get() = todayCourse != null
}
