package com.team_daytodo.daytodo.data.dto.record

import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import kotlinx.serialization.Serializable

@Serializable
data class PhotosDto(
    val courseId: Long,
    val photos: List<PhotoDto>,
)

@Serializable
data class PhotoDto(
    val memoryPhotoId: Long,
    val imageUrl: String,
    val photoOrder: Int,
)

fun PhotosDto.toDomain(): List<RecordPhoto> = photos.map { it.toDomain() }

fun PhotoDto.toDomain(): RecordPhoto = RecordPhoto(
    memoryPhotoId = memoryPhotoId,
    imageUrl = imageUrl,
    photoOrder = photoOrder,
)
