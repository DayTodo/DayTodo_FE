package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.auth.remote.AuthApi
import com.team_daytodo.daytodo.data.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRemoteModule {
    @Provides
    @Singleton
    fun provideAuthApi(
        retrofitFactory: RetrofitFactory,
        @DayTodoBaseUrl baseUrl: String,
    ): AuthApi = retrofitFactory
        .create(baseUrl)
        .create(AuthApi::class.java)
}
