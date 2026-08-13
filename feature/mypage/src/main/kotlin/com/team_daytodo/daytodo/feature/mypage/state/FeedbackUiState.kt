package com.team_daytodo.daytodo.feature.mypage.state

data class FeedbackUiState(
    val content: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
) {
    val canSubmit: Boolean
        get() = content.trim().length >= FeedbackMinContentLength && !isSubmitting
}

sealed interface FeedbackEvent {
    data class ShowMessage(val message: String) : FeedbackEvent
}

const val FeedbackMinContentLength = 100
