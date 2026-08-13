package com.team_daytodo.daytodo.data.region

import com.team_daytodo.daytodo.data.api.RegionApi
import com.team_daytodo.daytodo.data.dto.region.toDomain
import com.team_daytodo.daytodo.data.network.safeApiResult
import com.team_daytodo.daytodo.domain.region.model.Region
import com.team_daytodo.daytodo.domain.region.repository.RegionRepository
import javax.inject.Inject
import kotlinx.serialization.json.Json

class RegionRepositoryImpl @Inject constructor(
    private val regionApi: RegionApi,
    private val json: Json,
) : RegionRepository {
    override suspend fun getRegions(): Result<List<Region>> =
        safeApiResult(json) {
            regionApi.getRegions()
        }.mapCatching { response ->
            response.regions.map { it.toDomain() }
        }
}
