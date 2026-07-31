package com.team_daytodo.daytodo.domain.today.usecase

import com.team_daytodo.daytodo.domain.today.model.SavedMemoryPhotos
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject

class SaveMemoryPhotosUseCase @Inject constructor(
    private val todayRepository: TodayRepository,
) {
    suspend operator fun invoke(courseId: Long, imageUrls: List<String>): Result<SavedMemoryPhotos> =
        todayRepository.saveMemoryPhotos(courseId, imageUrls)
}
