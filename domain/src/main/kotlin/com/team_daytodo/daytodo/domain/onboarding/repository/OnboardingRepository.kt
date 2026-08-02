package com.team_daytodo.daytodo.domain.onboarding.repository

import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingPage

interface OnboardingRepository {
    suspend fun isOnboardingCompleted(): Result<Boolean>

    suspend fun getOnboardingPages(): Result<List<OnboardingPage>>

    suspend fun completeOnboarding(): Result<Unit>
}
