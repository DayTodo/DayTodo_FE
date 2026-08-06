package com.team_daytodo.daytodo.data.dto.mypage

import com.team_daytodo.daytodo.domain.mypage.model.InterestRegion
import kotlinx.serialization.Serializable

@Serializable
data class InterestRegionsResponse(
    val regions: List<InterestRegionDto>,
)

@Serializable
data class InterestRegionDto(
    val regionId: Long,
    val regionName: String,
    val parentRegionName: String,
)

@Serializable
data class UpdateInterestRegionsRequest(
    val regionIds: List<Long>,
)

fun InterestRegionsResponse.toDomain(): List<InterestRegion> = regions.map { it.toDomain() }

fun InterestRegionDto.toDomain(): InterestRegion = InterestRegion(
    regionId = regionId,
    regionName = regionName,
    parentRegionName = parentRegionName,
)
