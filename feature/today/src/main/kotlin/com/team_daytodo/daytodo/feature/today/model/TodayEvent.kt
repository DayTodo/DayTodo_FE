package com.team_daytodo.daytodo.feature.today.model

sealed interface TodayEvent {
    data class ShowMessage(val message: String) : TodayEvent
    data object MemoryPhotosSaved : TodayEvent
}
