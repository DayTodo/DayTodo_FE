package com.team_daytodo.daytodo.data.dto.today

import com.team_daytodo.daytodo.domain.today.model.MemoryPhoto
import com.team_daytodo.daytodo.domain.today.model.SavedMemoryPhotos
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

fun SaveMemoryPhotosResponse.toDomain(): SavedMemoryPhotos = SavedMemoryPhotos(
    savedCount = savedCount,
    photos = photos.map { it.toDomain() },
)

fun MemoryPhotoDto.toDomain(): MemoryPhoto = MemoryPhoto(
    memoryPhotoId = memoryPhotoId,
    imageUrl = imageUrl,
    photoOrder = photoOrder,
)
