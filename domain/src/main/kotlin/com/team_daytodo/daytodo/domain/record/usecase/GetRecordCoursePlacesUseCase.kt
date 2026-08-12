package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordPlace
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class GetRecordCoursePlacesUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(courseId: Long): Result<List<RecordPlace>> =
        recordRepository.getCoursePlaces(courseId)
}
