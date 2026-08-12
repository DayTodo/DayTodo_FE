package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegion
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class UpdateInterestRegionsUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(regionIds: List<Long>): Result<List<InterestRegion>> =
        mypageRepository.updateInterestRegions(regionIds)
}
