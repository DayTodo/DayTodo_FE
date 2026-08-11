package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegion
import com.team_daytodo.daytodo.domain.mypage.repository.MypageRepository
import javax.inject.Inject

class GetInterestRegionsUseCase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend operator fun invoke(): Result<List<InterestRegion>> = mypageRepository.getInterestRegions()
}
