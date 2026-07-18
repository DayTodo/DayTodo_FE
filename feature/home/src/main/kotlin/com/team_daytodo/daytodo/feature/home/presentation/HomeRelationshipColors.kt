package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun Relationship.accentColor(): Color =
    when (this) {
        Relationship.FRIEND -> DayTodoTheme.colors.brandSecondary
        Relationship.LOVER -> DayTodoTheme.colors.brandTertiary
        Relationship.FAMILY -> DayTodoTheme.colors.brandPrimary
    }

@Composable
internal fun Relationship.badgeColor(): Color =
    when (this) {
        Relationship.FRIEND -> DayTodoTheme.colors.brandSecondaryStrong
        Relationship.LOVER -> DayTodoTheme.colors.brandTertiaryStrong
        Relationship.FAMILY -> DayTodoTheme.colors.brandPrimaryStrong
    }

@Composable
internal fun Relationship.emphasisColor(): Color =
    when (this) {
        Relationship.FRIEND -> DayTodoTheme.colors.brandSecondaryEmphasis
        Relationship.LOVER -> DayTodoTheme.colors.brandTertiaryEmphasis
        Relationship.FAMILY -> DayTodoTheme.colors.brandPrimaryEmphasis
    }
