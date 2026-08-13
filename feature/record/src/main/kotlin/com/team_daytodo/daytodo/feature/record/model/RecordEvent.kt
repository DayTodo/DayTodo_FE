package com.team_daytodo.daytodo.feature.record.model

sealed interface RecordEvent {
    data class ShowMessage(val message: String) : RecordEvent
}
