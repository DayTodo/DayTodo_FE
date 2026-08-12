package com.team_daytodo.daytodo.domain.record.usecase

import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.domain.record.repository.RecordRepository
import javax.inject.Inject

class GetRecordPhotosUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {
    suspend operator fun invoke(courseId: Long): Result<List<RecordPhoto>> =
        recordRepository.getMemoryPhotos(courseId)
}
