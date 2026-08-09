package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class RemoveRecordPlaceBookmarkUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(bookmarkId: Long): Result<Unit> =
        recordRepository.removeBookmark(bookmarkId)
}
