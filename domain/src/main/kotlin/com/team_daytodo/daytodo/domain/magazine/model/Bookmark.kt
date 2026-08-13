package com.team_daytodo.daytodo.domain.magazine.model

data class Bookmark(
    val bookmarkId: Long,
    val magazineId: Long? = null,
    val placeId: Long? = null,
    val thumbnailUrl: String?,
    val placeName: String,
    val regionName: String,
    val category: String,
)
