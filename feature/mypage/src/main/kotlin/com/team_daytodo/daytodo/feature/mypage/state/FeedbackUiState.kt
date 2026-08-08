package com.team_daytodo.daytodo.feature.mypage.state

data class FeedbackUiState(
    val content: String = "",
    val isSubmitted: Boolean = false,
) {
    val canSubmit: Boolean
        get() = content.length >= FeedbackMinLength
}

private const val FeedbackMinLength = 100
