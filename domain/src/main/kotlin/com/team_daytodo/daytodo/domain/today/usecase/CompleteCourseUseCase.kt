package com.team_daytodo.daytodo.domain.today.usecase

import com.team_daytodo.daytodo.domain.today.model.CompletedCourse
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject

class CompleteCourseUseCase @Inject constructor(
    private val todayRepository: TodayRepository,
) {
    suspend operator fun invoke(courseId: Long): Result<CompletedCourse> =
        todayRepository.completeCourse(courseId)
}
