package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class WriteRecordDiaryUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(courseId: Long, content: String): Result<RecordDiary> =
        recordRepository.writeDiary(courseId, content)
}
