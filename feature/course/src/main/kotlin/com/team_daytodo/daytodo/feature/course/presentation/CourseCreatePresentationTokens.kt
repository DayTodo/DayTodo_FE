package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.core.model.Relationship

internal const val TotalInputSteps = 5

internal val ScreenHorizontalPadding = 20.dp

internal val FieldContentColor = Color(0xFF616166)
internal val SecondaryTextColor = Color(0xFF959595)
internal val PlaceholderColor = Color(0xFFC6C6C6)
internal val FieldBorderColor = Color(0xFFC1C1C1)
internal val ProgressTrackColor = Color(0xFFF3F4F6)
internal val ProgressColor = Color(0xFF8B8AF5)
internal val ParentRegionSelectedBackground = Color(0xFFE0E0F5)
internal val SundayColor = Color(0xFFFF5B00)
internal val LoadingActiveColor = Color(0xFF2D2D2D)
internal val LoadingInactiveColor = Color(0xFFD9D9D9)

internal data class RelationshipCardColors(
    val background: Color,
    val icon: Color,
)

internal fun relationshipDescription(relationship: Relationship): String =
    when (relationship) {
        Relationship.FRIEND -> "함께 즐기는 하루"
        Relationship.LOVER -> "둘만의 데이트"
        Relationship.FAMILY -> "편안한 나들이"
    }

internal fun relationshipColors(relationship: Relationship): RelationshipCardColors =
    when (relationship) {
        Relationship.FRIEND -> RelationshipCardColors(
            background = Color(0xFFF9F0BD),
            icon = Color(0xFFFBEEA2),
        )

        Relationship.LOVER -> RelationshipCardColors(
            background = Color(0xFFF5BDE4),
            icon = Color(0xFFF5A2DC),
        )

        Relationship.FAMILY -> RelationshipCardColors(
            background = Color(0xFFCBCAF5),
            icon = Color(0xFFB5B5F5),
        )
    }
