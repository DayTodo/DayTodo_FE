package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.ui.graphics.Color
import com.team_daytodo.daytodo.core.model.Relationship

internal data class HomeRelationshipColors(
    val background: Color,
    val emphasis: Color,
)

internal fun Relationship.homeRelationshipColors(): HomeRelationshipColors =
    when (this) {
        Relationship.FRIEND -> HomeRelationshipColors(
            background = Color(0xFFFFF9D8),
            emphasis = Color(0xFFFFAB00),
        )
        Relationship.LOVER -> HomeRelationshipColors(
            background = Color(0xFFFDE1F5),
            emphasis = Color(0xFFF56ACB),
        )
        Relationship.FAMILY -> HomeRelationshipColors(
            background = Color(0xFFE0E0F5),
            emphasis = Color(0xFF8B8AF5),
        )
    }
