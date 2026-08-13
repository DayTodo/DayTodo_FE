package com.team_daytodo.daytodo.domain.bookmark.model

data class Bookmark(
    val bookmarkId: Long,
    val magazineId: Long,
    val thumbnailUrl: String?,
    val placeName: String,
    val regionName: String,
    val category: String,
)
