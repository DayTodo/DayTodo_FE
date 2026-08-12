package com.team_daytodo.daytodo.domain.mypage.model

data class InterestRegion(
    val regionId: Long,
    val regionName: String,
    val parentRegionName: String?,
)
