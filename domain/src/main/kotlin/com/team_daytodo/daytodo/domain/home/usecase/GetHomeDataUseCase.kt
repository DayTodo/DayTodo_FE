package com.team_daytodo.daytodo.domain.home.usecase

import com.team_daytodo.daytodo.domain.home.model.HomeDashboard
import com.team_daytodo.daytodo.domain.home.repository.HomeRepository
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): Result<HomeDashboard> = runCatching {
        HomeDashboard(
            courses = homeRepository.getCourses().getOrThrow(),
            magazines = homeRepository.getTodayPickMagazines().getOrThrow(),
        )
    }
}
