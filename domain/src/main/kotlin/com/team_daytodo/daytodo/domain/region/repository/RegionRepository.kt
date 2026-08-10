package com.team_daytodo.daytodo.domain.region.repository

import com.team_daytodo.daytodo.domain.region.model.Region

interface RegionRepository {
    suspend fun getRegions(): Result<List<Region>>
}
