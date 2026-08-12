package com.team_daytodo.daytodo.domain.region.model

data class Region(
    val regionId: Long,
    val regionName: String,
    val regionLevel: RegionLevel,
    val parentRegionId: Long?,
    val parentRegionName: String?,
)

enum class RegionLevel {
    SIDO,
    SIGUNGU,
}
