package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.magazine.MagazineListRepositoryImpl
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MagazineDataModule {
    @Binds
    @Singleton
    abstract fun bindMagazineListRepository(
        magazineListRepositoryImpl: MagazineListRepositoryImpl,
    ): MagazineListRepository
}
