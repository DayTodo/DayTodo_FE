package com.team_daytodo.daytodo.feature.home.model

import com.team_daytodo.daytodo.domain.magazine.model.Magazine

data class HomeMagazineUiModel(
    val placeId: String,
    val title: String,
    val location: String,
    val description: String,
    val thumbnailUrl: String?,
)

fun Magazine.toHomeUiModel(): HomeMagazineUiModel = HomeMagazineUiModel(
    placeId = magazineId.toString(),
    title = placeName,
    location = regionName,
    description = tagline.orEmpty(),
    thumbnailUrl = thumbnailUrl,
)
