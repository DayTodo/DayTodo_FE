package com.team_daytodo.daytodo.domain.onboarding.usecase

import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingPage
import com.team_daytodo.daytodo.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class IsOnboardingCompletedUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) {
    suspend operator fun invoke(): Result<Boolean> =
        onboardingRepository.isOnboardingCompleted()
}

class GetOnboardingPagesUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) {
    suspend operator fun invoke(): Result<List<OnboardingPage>> =
        onboardingRepository.getOnboardingPages()
}

class CompleteOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        onboardingRepository.completeOnboarding()
}
