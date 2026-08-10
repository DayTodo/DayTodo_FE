package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.onboarding.DummyOnboardingRepository
import com.team_daytodo.daytodo.domain.onboarding.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingDataModule {
    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        dummyOnboardingRepository: DummyOnboardingRepository,
    ): OnboardingRepository
}
