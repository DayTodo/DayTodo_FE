package com.team_daytodo.daytodo.domain.magazine.model

data class Magazine(
    val magazineId: Long,
    val thumbnailUrl: String?,
    val placeName: String,
    val regionName: String,
    val tagline: String?,
    val isAd: Boolean,
)
