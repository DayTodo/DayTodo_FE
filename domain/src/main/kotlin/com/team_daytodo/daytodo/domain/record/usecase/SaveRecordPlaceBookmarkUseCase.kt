package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordPlaceBookmark
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class SaveRecordPlaceBookmarkUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(placeId: Long): Result<RecordPlaceBookmark> =
        recordRepository.saveBookmark(placeId)
}
