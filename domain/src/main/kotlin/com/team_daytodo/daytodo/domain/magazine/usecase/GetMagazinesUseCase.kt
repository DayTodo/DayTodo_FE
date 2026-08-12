package com.team_daytodo.daytodo.domain.magazine.usecase

import com.team_daytodo.daytodo.domain.magazine.model.Magazine
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineListRepository
import javax.inject.Inject

class GetMagazinesUseCase @Inject constructor(
    private val magazineListRepository: MagazineListRepository,
) {
    suspend operator fun invoke(): Result<List<Magazine>> =
        magazineListRepository.getMagazines()
}
