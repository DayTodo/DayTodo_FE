package com.team_daytodo.daytodo.feature.onboarding.model

import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingPage

data class OnboardingUiState(
    val pages: List<OnboardingPage> = emptyList(),
    val currentPageIndex: Int = 0,
    val isLoading: Boolean = true,
    val isCompleting: Boolean = false,
    val errorMessage: String? = null,
) {
    val pageCount: Int
        get() = pages.size

    val currentPage: OnboardingPage?
        get() = pages.getOrNull(currentPageIndex)

    val isLastPage: Boolean
        get() = currentPageIndex == pages.lastIndex && pages.isNotEmpty()
}

sealed interface OnboardingEvent {
    data object Completed : OnboardingEvent

    data class ShowMessage(val message: String) : OnboardingEvent
}
