package com.team_daytodo.daytodo.feature.mypage.state

sealed interface PolicyDocumentUiState {
    data object Loading : PolicyDocumentUiState
    data class Content(val body: String) : PolicyDocumentUiState
    data object Error : PolicyDocumentUiState
}
