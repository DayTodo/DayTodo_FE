package com.team_daytodo.daytodo.feature.onboarding.model

import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingGuide
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingPage

internal fun OnboardingPage.toUiModel(): OnboardingPageUiModel {
    val spec = id.toOnboardingPresentationSpec()

    return OnboardingPageUiModel(
        id = id,
        headline = headline,
        description = description,
        illustration = spec.illustration,
        template = spec.template,
        guide = guide?.toUiModel(),
        canSkip = canSkip,
    )
}

private fun OnboardingGuide.toUiModel(): OnboardingGuideUiModel =
    OnboardingGuideUiModel(
        step = step,
        title = title,
        description = description,
    )
