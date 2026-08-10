package com.team_daytodo.daytodo.feature.course.presentation.defaults

import androidx.compose.ui.graphics.Color
import com.team_daytodo.daytodo.core.model.Relationship

internal data class RelationshipColors(
    val background: Color,
    val icon: Color,
    val text: Color,
)

internal fun relationshipDescription(relationship: Relationship): String =
    when (relationship) {
        Relationship.FRIEND -> "함께 즐기는 하루"
        Relationship.LOVER -> "둘만의 데이트"
        Relationship.FAMILY -> "편안한 나들이"
    }

internal fun relationshipColors(relationship: Relationship): RelationshipColors =
    when (relationship) {
        Relationship.FRIEND -> RelationshipColors(
            background = Color(0xFFFFFCED),
            icon = Color(0xFFFFE96A),
            text = Color(0xFFFFAB00),
        )

        Relationship.LOVER -> RelationshipColors(
            background = Color(0xFFFFF5FC),
            icon = Color(0xFFF56ACB),
            text = Color(0xFFF56ACB),
        )

        Relationship.FAMILY -> RelationshipColors(
            background = Color(0xFFF3F3FD),
            icon = Color(0xFF8B8AF5),
            text = Color(0xFF8B8AF5),
        )
    }
