package com.team_daytodo.daytodo.feature.onboarding.model

sealed interface OnboardingEvent {
    data object Completed : OnboardingEvent

    data class ShowMessage(val message: String) : OnboardingEvent
}
