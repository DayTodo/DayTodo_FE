package com.team_daytodo.daytodo.feature.onboarding.model

data class OnboardingUiState(
    val pages: List<OnboardingPageUiModel> = emptyList(),
    val currentPageIndex: Int = 0,
    val isLoading: Boolean = true,
    val isCompleting: Boolean = false,
    val errorMessage: String? = null,
) {
    val pageCount: Int
        get() = pages.size

    val currentPage: OnboardingPageUiModel?
        get() = pages.getOrNull(currentPageIndex)

    val isLastPage: Boolean
        get() = currentPageIndex == pages.lastIndex && pages.isNotEmpty()
}
