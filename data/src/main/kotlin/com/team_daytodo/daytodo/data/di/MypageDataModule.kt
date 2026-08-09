package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.mypage.MypageRepositoryImpl
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MypageDataModule {
    @Binds
    @Singleton
    abstract fun bindMypageRepository(
        mypageRepositoryImpl: MypageRepositoryImpl,
    ): MypageRepository
}
