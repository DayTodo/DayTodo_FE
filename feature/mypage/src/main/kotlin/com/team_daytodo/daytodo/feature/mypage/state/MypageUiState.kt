package com.team_daytodo.daytodo.feature.mypage.state

data class MypageUiState(
    val nickname: String = "",
    val notificationEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val dialogState: MypageDialogState = MypageDialogState.None,
    val errorMessage: String? = null,
)
