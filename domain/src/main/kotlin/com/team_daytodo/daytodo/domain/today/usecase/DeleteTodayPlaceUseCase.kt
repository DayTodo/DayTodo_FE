package com.team_daytodo.daytodo.domain.today.usecase

import com.team_daytodo.daytodo.domain.today.model.TodayCoursePlace
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject

class DeleteTodayPlaceUseCase @Inject constructor(
    private val todayRepository: TodayRepository,
) {
    suspend operator fun invoke(courseId: Long, coursePlaceId: Long): Result<List<TodayCoursePlace>> =
        todayRepository.deleteCoursePlace(courseId, coursePlaceId)
}
