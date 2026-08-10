package com.team_daytodo.daytodo.domain.magazine.repository

import com.team_daytodo.daytodo.domain.magazine.model.Bookmark
import com.team_daytodo.daytodo.domain.magazine.model.Magazine
import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType

interface MagazineListRepository {
    suspend fun getMagazines(): Result<List<Magazine>>

    suspend fun getMagazineDetail(magazineId: Long): Result<MagazineDetail>

    suspend fun getBookmarks(sortType: SavedPlaceSortType): Result<List<Bookmark>>

    // 저장(북마크) 생성. 성공 시 저장 해제(delete)에 필요한 bookmarkId를 반환한다.
    suspend fun createBookmark(magazineId: Long): Result<Long>

    suspend fun deleteBookmark(bookmarkId: Long): Result<Unit>
}
