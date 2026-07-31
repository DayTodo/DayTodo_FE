package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.calendar.DummyCalendarRepository
import com.team_daytodo.daytodo.domain.calendar.repository.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarDataModule {
    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        dummyCalendarRepository: DummyCalendarRepository,
    ): CalendarRepository
}
