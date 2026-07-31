package com.team_daytodo.daytodo.domain.onboarding.model

data class OnboardingPage(
    val id: String,
    val headline: String,
    val description: String,
    val guide: OnboardingGuide? = null,
    val canSkip: Boolean = true,
)

data class OnboardingGuide(
    val step: Int,
    val title: String,
    val description: String,
)
