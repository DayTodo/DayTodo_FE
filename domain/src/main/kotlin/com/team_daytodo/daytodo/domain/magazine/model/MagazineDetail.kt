package com.team_daytodo.daytodo.domain.magazine.model

data class MagazineDetail(
    val magazineId: Long,
    val thumbnailUrl: String?,
    val category: String,
    val placeName: String,
    val address: String,
    val businessHours: String?,
    val phone: String?,
    val content: String?,
    val photos: List<MagazinePhoto>,
)

data class MagazinePhoto(
    val imageId: String,
    val imageUrl: String,
    val imageOrder: Int?,
)
