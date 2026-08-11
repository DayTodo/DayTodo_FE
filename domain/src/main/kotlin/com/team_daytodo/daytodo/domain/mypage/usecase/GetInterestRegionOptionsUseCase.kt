package com.team_daytodo.daytodo.domain.mypage.usecase

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegionOption
import com.team_daytodo.daytodo.domain.region.model.Region
import com.team_daytodo.daytodo.domain.region.usecase.GetRegionsUseCase
import javax.inject.Inject

class GetInterestRegionOptionsUseCase @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase,
) {
    suspend operator fun invoke(): Result<List<InterestRegionOption>> =
        getRegionsUseCase().map { regions -> regions.map { it.toInterestRegionOption() } }
}

private fun Region.toInterestRegionOption(): InterestRegionOption =
    InterestRegionOption(
        regionId = regionId,
        regionName = regionName,
        parentRegionName = parentRegionName,
    )
