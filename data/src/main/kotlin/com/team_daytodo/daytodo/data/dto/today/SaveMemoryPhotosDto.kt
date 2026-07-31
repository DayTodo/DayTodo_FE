package com.team_daytodo.daytodo.data.dto.today

import kotlinx.serialization.Serializable

@Serializable
data class SaveMemoryPhotosRequest(
    val imageUrls: List<String>,
)

@Serializable
data class SaveMemoryPhotosResponse(
    val savedCount: Int,
    val photos: List<MemoryPhotoDto>,
)

@Serializable
data class MemoryPhotoDto(
    val memoryPhotoId: Long,
    val imageUrl: String,
    val photoOrder: Int,
)
