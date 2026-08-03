package com.team_daytodo.daytodo.feature.mypage.state

data class ProfileEditUiState(
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val linkedAccountProvider: String = "",
    val linkedAccountId: String = "",
    val isAccountLinked: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
