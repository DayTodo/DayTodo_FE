package com.team_daytodo.daytodo.data.dto.magazine

import com.team_daytodo.daytodo.domain.magazine.model.Magazine
import kotlinx.serialization.Serializable

@Serializable
data class GetMagazinesResponse(
    val magazines: List<MagazineItemDto>,
)

@Serializable
data class MagazineItemDto(
    val magazineId: Long,
    val thumbnailUrl: String? = null,
    val placeName: String,
    val regionName: String,
    val tagline: String? = null,
    val isAd: Boolean,
)

fun MagazineItemDto.toDomain(): Magazine = Magazine(
    magazineId = magazineId,
    thumbnailUrl = thumbnailUrl,
    placeName = placeName,
    regionName = regionName,
    tagline = tagline,
    isAd = isAd,
)
