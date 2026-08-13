package com.team_daytodo.daytodo.domain.bookmark.usecase

import com.team_daytodo.daytodo.domain.bookmark.model.Bookmark
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.bookmark.repository.BookmarkRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(
        sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
        regionId: Long? = null,
    ): Result<List<Bookmark>> = bookmarkRepository.getBookmarks(sortType, regionId)
}

class CreateBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(magazineId: Long): Result<Long> =
        bookmarkRepository.createBookmark(magazineId)
}

class DeleteBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(bookmarkId: Long): Result<Unit> =
        bookmarkRepository.deleteBookmark(bookmarkId)
}
