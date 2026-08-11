package com.team_daytodo.daytodo.feature.home.model

data class HomeUiState(
    val username: String,
    val interestLocation: String,
    val todayCourse: TodayCourse?,
    val upcomingCourse: UpcomingCourse?,
    val createdCourses: List<CreatedCourse>,
    val todayPickMagazines: List<HomeMagazineUiModel> = emptyList(),
    val isMagazineLoading: Boolean = false,
    val magazineErrorMessage: String? = null,
) {
    val hasTodaySchedule: Boolean
        get() = todayCourse != null
}
