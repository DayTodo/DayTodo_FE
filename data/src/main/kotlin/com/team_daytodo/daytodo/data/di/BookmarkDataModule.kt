package com.team_daytodo.daytodo.data.di

import com.team_daytodo.daytodo.data.bookmark.BookmarkRepositoryImpl
import com.team_daytodo.daytodo.domain.bookmark.repository.BookmarkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkDataModule {
    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl,
    ): BookmarkRepository
}
