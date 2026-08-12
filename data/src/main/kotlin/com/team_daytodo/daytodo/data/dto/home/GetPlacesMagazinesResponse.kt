package com.team_daytodo.daytodo.data.dto.home

import com.team_daytodo.daytodo.domain.home.model.HomeMagazine
import kotlinx.serialization.Serializable

@Serializable
data class GetPlacesMagazinesResponse(
    val magazines: List<HomeMagazineDto> = emptyList(),
)

@Serializable
data class HomeMagazineDto(
    val magazineId: Long,
    val thumbnailUrl: String? = null,
    val placeName: String,
    val regionName: String,
    val tagline: String,
    val isAd: Boolean = false,
)

fun GetPlacesMagazinesResponse.toDomain(): List<HomeMagazine> =
    magazines.map { it.toDomain() }

private fun HomeMagazineDto.toDomain(): HomeMagazine = HomeMagazine(
    magazineId = magazineId,
    thumbnailUrl = thumbnailUrl,
    placeName = placeName,
    regionName = regionName,
    tagline = tagline,
    isAd = isAd,
)
