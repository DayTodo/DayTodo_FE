package com.team_daytodo.daytodo.data.region

import com.team_daytodo.daytodo.data.api.RegionApi
import com.team_daytodo.daytodo.data.dto.region.toDomain
import com.team_daytodo.daytodo.domain.region.model.Region
import com.team_daytodo.daytodo.domain.region.repository.RegionRepository
import javax.inject.Inject

class RegionRepositoryImpl @Inject constructor(
    private val regionApi: RegionApi,
) : RegionRepository {
    override suspend fun getRegions(): Result<List<Region>> = runCatching {
        regionApi.getRegions().regions.map { it.toDomain() }
    }
}
