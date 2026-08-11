package com.team_daytodo.daytodo.data.dto.region

import com.team_daytodo.daytodo.domain.region.model.Region
import com.team_daytodo.daytodo.domain.region.model.RegionLevel
import kotlinx.serialization.Serializable

@Serializable
data class GetRegionsResponse(
    val regions: List<RegionItemDto>,
)

@Serializable
data class RegionItemDto(
    val regionId: Long,
    val regionName: String,
    val regionLevel: RegionLevelDto,
    val parentRegionId: Long? = null,
    val parentRegionName: String? = null,
)

@Serializable
enum class RegionLevelDto {
    SIDO,
    SIGUNGU,
}

fun RegionItemDto.toDomain(): Region = Region(
    regionId = regionId,
    regionName = regionName,
    regionLevel = when (regionLevel) {
        RegionLevelDto.SIDO -> RegionLevel.SIDO
        RegionLevelDto.SIGUNGU -> RegionLevel.SIGUNGU
    },
    parentRegionId = parentRegionId,
    parentRegionName = parentRegionName,
)
