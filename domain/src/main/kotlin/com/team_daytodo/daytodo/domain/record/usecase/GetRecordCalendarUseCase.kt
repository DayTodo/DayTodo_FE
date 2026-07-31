package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordCalendarData
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class GetRecordCalendarUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(): Result<RecordCalendarData> = recordRepository.getRecordCalendar()
}
