package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.auth.AuthTokenRepositoryImpl
import com.team_daytodo.daytodo.domain.auth.repository.AuthTokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthTokenDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenRepository(
        authTokenRepositoryImpl: AuthTokenRepositoryImpl,
    ): AuthTokenRepository
}
