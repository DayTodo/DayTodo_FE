package com.team_daytodo.daytodo.domain.today.usecase

import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject

class ReorderTodayPlacesUseCase @Inject constructor(
    private val todayRepository: TodayRepository,
) {
    suspend operator fun invoke(courseId: Long, orderedCoursePlaceIds: List<Long>): Result<List<TodayCoursePlace>> =
        todayRepository.reorderCoursePlaces(courseId, orderedCoursePlaceIds)
}
