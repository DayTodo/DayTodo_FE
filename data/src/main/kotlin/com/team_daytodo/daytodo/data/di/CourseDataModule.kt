package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.course.DummyCourseRepository
import com.team_daytodo.daytodo.domain.course.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CourseDataModule {
    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        dummyCourseRepository: DummyCourseRepository,
    ): CourseRepository
}
