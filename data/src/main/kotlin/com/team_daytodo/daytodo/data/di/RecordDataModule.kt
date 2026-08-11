package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.record.RecordRepositoryImpl
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecordDataModule {
    @Binds
    @Singleton
    abstract fun bindRecordRepository(
        recordRepositoryImpl: RecordRepositoryImpl,
    ): RecordRepository
}
