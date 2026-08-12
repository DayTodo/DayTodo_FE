package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordDiary
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import java.time.LocalDate
import javax.inject.Inject

class GetRecordDiaryUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(date: LocalDate): Result<RecordDiary?> =
        recordRepository.getDiaryByDate(date)
}
