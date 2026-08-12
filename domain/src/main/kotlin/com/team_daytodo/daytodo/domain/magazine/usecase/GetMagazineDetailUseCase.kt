package com.team_daytodo.daytodo.domain.magazine.usecase

import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail
import com.team_daytodo.daytodo.domain.magazine.repository.MagazineListRepository
import javax.inject.Inject

class GetMagazineDetailUseCase @Inject constructor(
    private val magazineListRepository: MagazineListRepository,
) {
    suspend operator fun invoke(magazineId: Long): Result<MagazineDetail> =
        magazineListRepository.getMagazineDetail(magazineId)
}
