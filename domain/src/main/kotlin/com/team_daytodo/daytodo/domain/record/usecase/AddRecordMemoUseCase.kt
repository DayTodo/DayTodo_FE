package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordMemo
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class AddRecordMemoUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(photoId: String, author: String, content: String): Result<RecordMemo> =
        recordRepository.addMemo(photoId = photoId, author = author, content = content)
}
