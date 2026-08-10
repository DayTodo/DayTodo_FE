package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.viewmodel.PhoneChangeViewModel

@Composable
fun PhoneChangeRoute(
    onBackClick: () -> Unit = {},
    viewModel: PhoneChangeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PhoneChangeScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onNewPhoneNumberChange = viewModel::onNewPhoneNumberChange,
        onVerificationCodeChange = viewModel::onVerificationCodeChange,
        onRequestVerificationCodeClick = viewModel::onRequestVerificationCodeClick,
        onChangeClick = viewModel::onChangeClick,
        onSuccessDialogDismissed = viewModel::onSuccessDialogDismissed,
    )
}
