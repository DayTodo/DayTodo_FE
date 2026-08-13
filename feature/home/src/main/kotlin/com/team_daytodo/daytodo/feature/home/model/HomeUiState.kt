package com.team_daytodo.daytodo.feature.home.model

data class HomeUiState(
    val username: String = "",
    val bannerMessage: String = "",
    val interestLocation: String = "",
    val todayCourse: TodayCourse? = null,
    val upcomingCourse: UpcomingCourse? = null,
    val createdCourses: List<CreatedCourse> = emptyList(),
    val todayPickMagazines: List<HomeMagazineUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val shouldRequestInterestRegionSetup: Boolean = false,
    val interestRegionGroups: List<HomeInterestRegionGroup> = emptyList(),
    val selectedInterestRegionGroupName: String = "",
    val selectedInterestRegionOptionKey: String? = null,
    val selectedInterestRegionIds: Set<Long> = emptySet(),
    val showInterestRegionDialog: Boolean = false,
    val isInterestRegionSaving: Boolean = false,
    val interestRegionErrorMessage: String? = null,
) {
    val hasTodaySchedule: Boolean
        get() = todayCourse != null

    val selectedInterestRegionGroup: HomeInterestRegionGroup?
        get() = interestRegionGroups.firstOrNull { it.parentName == selectedInterestRegionGroupName }

    val selectedInterestRegionOption: HomeInterestRegionOption?
        get() = interestRegionGroups
            .flatMap { it.options }
            .firstOrNull { it.key == selectedInterestRegionOptionKey }

    val canSaveInterestRegions: Boolean
        get() = selectedInterestRegionOption?.regionId != null && !isInterestRegionSaving
}

data class HomeInterestRegionGroup(
    val parentName: String,
    val displayName: String = parentName,
    val options: List<HomeInterestRegionOption>,
)

data class HomeInterestRegionOption(
    val key: String,
    val displayName: String,
    val regionId: Long?,
)
