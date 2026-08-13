package com.team_daytodo.daytodo.data.dto.magazine

import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail
import com.team_daytodo.daytodo.domain.magazine.model.MagazinePhoto
import kotlinx.serialization.Serializable

@Serializable
data class GetMagazineDetailResponse(
    val magazineId: Long,
    val thumbnailUrl: String? = null,
    val category: String? = null,
    val placeName: String,
    val address: String? = null,
    val businessHours: String? = null,
    val phone: String? = null,
    val content: String? = null,
    val photos: List<PhotoItemDto> = emptyList(),
)

@Serializable
data class PhotoItemDto(
    val imageId: String,
    val imageUrl: String,
    val imageOrder: Int? = null,
)

fun GetMagazineDetailResponse.toDomain(): MagazineDetail = MagazineDetail(
    magazineId = magazineId,
    thumbnailUrl = thumbnailUrl,
    category = category.orEmpty(),
    placeName = placeName,
    address = address.orEmpty(),
    businessHours = businessHours,
    phone = phone,
    content = content,
    photos = photos.map { it.toDomain() },
)

fun PhotoItemDto.toDomain(): MagazinePhoto = MagazinePhoto(
    imageId = imageId,
    imageUrl = imageUrl,
    imageOrder = imageOrder,
)
