package com.team_daytodo.daytodo.domain.region.usecase

import com.team_daytodo.daytodo.domain.region.model.Region
import com.team_daytodo.daytodo.domain.region.repository.RegionRepository
import javax.inject.Inject

class GetRegionsUseCase @Inject constructor(
    private val regionRepository: RegionRepository,
) {
    suspend operator fun invoke(): Result<List<Region>> = regionRepository.getRegions()
}
