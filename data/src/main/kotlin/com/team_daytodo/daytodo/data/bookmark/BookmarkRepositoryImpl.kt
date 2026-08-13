package com.team_daytodo.daytodo.data.bookmark

import com.team_daytodo.daytodo.data.api.BookmarkApi
import com.team_daytodo.daytodo.data.dto.bookmark.CreateBookmarkRequest
import com.team_daytodo.daytodo.data.dto.bookmark.toDomain
import com.team_daytodo.daytodo.domain.bookmark.model.Bookmark
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.bookmark.repository.BookmarkRepository
import javax.inject.Inject
import retrofit2.HttpException
import retrofit2.Response

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkApi: BookmarkApi,
) : BookmarkRepository {
    override suspend fun getBookmarks(
        sortType: SavedPlaceSortType,
        regionId: Long?,
    ): Result<List<Bookmark>> = runCatching {
        bookmarkApi.getBookmarks(sort = sortType.toBookmarkSortParam(), regionId = regionId)
            .bookmarks
            .map { it.toDomain() }
    }

    override suspend fun createBookmark(magazineId: Long): Result<Long> = runCatching {
        bookmarkApi.createBookmark(CreateBookmarkRequest(contentId = magazineId)).bookmarkId
    }

    override suspend fun deleteBookmark(bookmarkId: Long): Result<Unit> = runCatching {
        bookmarkApi.deleteBookmark(bookmarkId).throwIfNotSuccessful()
    }

    private fun Response<Unit>.throwIfNotSuccessful() {
        if (!isSuccessful) throw HttpException(this)
    }

    private fun SavedPlaceSortType.toBookmarkSortParam(): String = when (this) {
        SavedPlaceSortType.RecentSaved -> "RECENT"
        SavedPlaceSortType.OldestSaved -> "OLDEST"
        SavedPlaceSortType.Name -> "NAME"
        SavedPlaceSortType.Popularity -> "POPULAR"
    }
}
