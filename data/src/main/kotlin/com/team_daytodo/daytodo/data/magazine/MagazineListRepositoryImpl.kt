package com.team_daytodo.daytodo.data.magazine

import com.team_daytodo.daytodo.data.api.MagazineApi
import com.team_daytodo.daytodo.data.dto.magazine.CreateBookmarkRequest
import com.team_daytodo.daytodo.data.dto.magazine.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
import com.team_daytodo.daytodo.data.network.successOrThrow
import com.team_daytodo.daytodo.domain.magazine.model.Bookmark
import com.team_daytodo.daytodo.domain.magazine.model.Magazine
import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineListRepository
import javax.inject.Inject
import kotlinx.serialization.json.Json

class MagazineListRepositoryImpl @Inject constructor(
    private val magazineApi: MagazineApi,
    private val json: Json,
) : MagazineListRepository {
    override suspend fun getMagazines(): Result<List<Magazine>> =
        safeApiResult(json) {
            magazineApi.getMagazines()
        }.mapCatching { response ->
            response.magazines.map { it.toDomain() }
        }

    override suspend fun getMagazineDetail(magazineId: Long): Result<MagazineDetail> =
        safeApiResult(json) {
            magazineApi.getMagazineDetail(magazineId)
        }.mapCatching {
            it.toDomain()
        }

    override suspend fun getBookmarks(sortType: SavedPlaceSortType): Result<List<Bookmark>> =
        safeApiResult(json) {
            magazineApi.getBookmarks(sort = sortType.toBookmarkSortParam(), regionId = null)
        }.mapCatching { response ->
            response.bookmarks.map { it.toDomain() }
        }

    override suspend fun createBookmark(magazineId: Long): Result<Long> =
        safeApiResult(json) {
            magazineApi.createBookmark(CreateBookmarkRequest(contentId = magazineId))
        }.mapCatching {
            it.bookmarkId
        }

    override suspend fun deleteBookmark(bookmarkId: Long): Result<Unit> = safeApiResult(json) {
        magazineApi.deleteBookmark(bookmarkId).successOrThrow(json)
    }

    private fun SavedPlaceSortType.toBookmarkSortParam(): String = when (this) {
        SavedPlaceSortType.RecentSaved -> "RECENT"
        SavedPlaceSortType.OldestSaved -> "OLDEST"
        SavedPlaceSortType.Name -> "NAME"
        SavedPlaceSortType.Popularity -> "POPULAR"
    }
}
