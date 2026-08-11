package com.team_daytodo.daytodo.data.magazine

import com.team_daytodo.daytodo.data.api.MagazineApi
import com.team_daytodo.daytodo.data.dto.magazine.CreateBookmarkRequest
import com.team_daytodo.daytodo.data.dto.magazine.toDomain
import com.team_daytodo.daytodo.domain.magazine.model.Bookmark
import com.team_daytodo.daytodo.domain.magazine.model.Magazine
import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineListRepository
import javax.inject.Inject
import retrofit2.HttpException
import retrofit2.Response

class MagazineListRepositoryImpl @Inject constructor(
    private val magazineApi: MagazineApi,
) : MagazineListRepository {
    override suspend fun getMagazines(): Result<List<Magazine>> = runCatching {
        magazineApi.getMagazines().magazines.map { it.toDomain() }
    }

    override suspend fun getMagazineDetail(magazineId: Long): Result<MagazineDetail> = runCatching {
        magazineApi.getMagazineDetail(magazineId).toDomain()
    }

    override suspend fun getBookmarks(sortType: SavedPlaceSortType): Result<List<Bookmark>> = runCatching {
        magazineApi.getBookmarks(sort = sortType.toBookmarkSortParam(), regionId = null)
            .bookmarks
            .map { it.toDomain() }
    }

    override suspend fun createBookmark(magazineId: Long): Result<Long> = runCatching {
        magazineApi.createBookmark(CreateBookmarkRequest(contentId = magazineId)).bookmarkId
    }

    override suspend fun deleteBookmark(bookmarkId: Long): Result<Unit> = runCatching {
        magazineApi.deleteBookmark(bookmarkId).throwIfNotSuccessful()
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
