package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.region.RegionRepositoryImpl
import com.team_daytodo.daytodo.domain.region.repository.RegionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RegionDataModule {
    @Binds
    @Singleton
    abstract fun bindRegionRepository(
        regionRepositoryImpl: RegionRepositoryImpl,
    ): RegionRepository
}
