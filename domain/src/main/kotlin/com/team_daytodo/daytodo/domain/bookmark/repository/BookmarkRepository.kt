package com.team_daytodo.daytodo.domain.bookmark.repository

import com.team_daytodo.daytodo.domain.bookmark.model.Bookmark
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType

interface BookmarkRepository {
    suspend fun getBookmarks(sortType: SavedPlaceSortType, regionId: Long? = null): Result<List<Bookmark>>

    // 저장(북마크) 생성. 성공 시 저장 해제(delete)에 필요한 bookmarkId를 반환한다.
    suspend fun createBookmark(magazineId: Long): Result<Long>

    suspend fun deleteBookmark(bookmarkId: Long): Result<Unit>
}
