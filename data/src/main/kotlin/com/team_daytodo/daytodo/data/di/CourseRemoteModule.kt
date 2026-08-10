package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.course.remote.CourseApi
import com.team_daytodo.daytodo.data.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseRemoteModule {
    @Provides
    @Singleton
    fun provideCourseApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): CourseApi = retrofitFactory
        .create(baseUrl)
        .create(CourseApi::class.java)
}
