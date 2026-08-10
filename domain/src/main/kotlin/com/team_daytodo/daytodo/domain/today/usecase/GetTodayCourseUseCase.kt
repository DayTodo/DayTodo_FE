package com.team_daytodo.daytodo.domain.today.usecase

import com.team_daytodo.daytodo.domain.today.model.TodayCourse
import com.team_daytodo.daytodo.domain.today.repository.TodayRepository
import javax.inject.Inject

class GetTodayCourseUseCase @Inject constructor(
    private val todayRepository: TodayRepository,
) {
    suspend operator fun invoke(): Result<TodayCourse?> = todayRepository.getTodayCourse()
}
