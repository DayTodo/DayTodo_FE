package com.team_daytodo.daytodo.feature.home.model

import com.team_daytodo.daytodo.uikit.R as UIKitR

data class HomeMagazineUiModel(
    val placeId: String,
    val title: String,
    val location: String,
    val description: String,
    val thumbnailUrl: String? = null,
    val imageRes: Int = UIKitR.drawable.ic_symbol,
)
