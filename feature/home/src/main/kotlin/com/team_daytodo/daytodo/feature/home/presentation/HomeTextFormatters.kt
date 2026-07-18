package com.team_daytodo.daytodo.feature.home.presentation

import com.team_daytodo.daytodo.core.model.Relationship

internal val Relationship.courseCompanionLabel: String
    get() = when (this) {
        Relationship.FRIEND -> "친구들"
        Relationship.LOVER -> "연인"
        Relationship.FAMILY -> "가족"
    }

internal val Relationship.scheduleTargetLabel: String
    get() = when (this) {
        Relationship.FRIEND -> "친구와"
        Relationship.LOVER -> "연인과"
        Relationship.FAMILY -> "가족과"
    }
