package com.team_daytodo.daytodo.feature.mypage.state

data class PhoneChangeUiState(
    val currentPhoneNumber: String = "",
    val newPhoneNumber: String = "",
    val verificationCode: String = "",
    val isLoading: Boolean = false,
    val isChangeSuccess: Boolean = false,
    val errorMessage: String? = null,
)
