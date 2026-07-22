package com.team_daytodo.daytodo.core.model

enum class Relationship(
    val label: String,
    val companionLabel: String,
    val scheduleTargetLabel: String,
) {
    FRIEND(
        label = "친구",
        companionLabel = "친구들",
        scheduleTargetLabel = "친구와",
    ),
    LOVER(
        label = "연인",
        companionLabel = "연인",
        scheduleTargetLabel = "연인과",
    ),
    FAMILY(
        label = "가족",
        companionLabel = "가족",
        scheduleTargetLabel = "가족과",
    ),
}
