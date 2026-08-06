package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.today.TodayRepositoryImpl
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDataModule {
    @Binds
    @Singleton
    abstract fun bindTodayRepository(
        todayRepositoryImpl: TodayRepositoryImpl,
    ): TodayRepository
}
