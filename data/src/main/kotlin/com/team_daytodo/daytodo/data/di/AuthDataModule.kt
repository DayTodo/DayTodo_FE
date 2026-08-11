package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.auth.AuthRepositoryImpl
import com.team_daytodo.daytodo.domain.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepository: AuthRepositoryImpl,
    ): AuthRepository
}
