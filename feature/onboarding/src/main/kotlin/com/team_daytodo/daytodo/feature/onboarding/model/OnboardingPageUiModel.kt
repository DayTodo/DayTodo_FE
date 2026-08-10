package com.team_daytodo.daytodo.feature.onboarding.model

import androidx.annotation.DrawableRes

data class OnboardingPageUiModel(
    val id: String,
    val headline: String,
    val description: String,
    val illustration: OnboardingIllustration,
    val template: OnboardingPageTemplate,
    val guide: OnboardingGuideUiModel? = null,
    val canSkip: Boolean = true,
)

data class OnboardingGuideUiModel(
    val step: Int,
    val title: String,
    val description: String,
)

data class OnboardingIllustration(
    @param:DrawableRes val primaryImageRes: Int,
    @param:DrawableRes val secondaryImageRes: Int? = null,
    val contentDescription: String? = null,
)
