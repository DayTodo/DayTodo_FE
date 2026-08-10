package com.team_daytodo.daytodo.domain.magazine.usecase

import com.team_daytodo.daytodo.domain.magazine.model.Bookmark
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineListRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val magazineListRepository: MagazineListRepository,
) {
    suspend operator fun invoke(
        sortType: SavedPlaceSortType = SavedPlaceSortType.RecentSaved,
    ): Result<List<Bookmark>> = magazineListRepository.getBookmarks(sortType)
}

class CreateBookmarkUseCase @Inject constructor(
    private val magazineListRepository: MagazineListRepository,
) {
    suspend operator fun invoke(magazineId: Long): Result<Long> =
        magazineListRepository.createBookmark(magazineId)
}

class DeleteBookmarkUseCase @Inject constructor(
    private val magazineListRepository: MagazineListRepository,
) {
    suspend operator fun invoke(bookmarkId: Long): Result<Unit> =
        magazineListRepository.deleteBookmark(bookmarkId)
}
