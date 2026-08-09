package com.team_daytodo.daytodo.feature.mypage.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.state.PasswordChangeEvent
import com.team_daytodo.daytodo.feature.mypage.viewmodel.PasswordChangeViewModel

@Composable
fun PasswordChangeRoute(
    onBackClick: () -> Unit,
    onPasswordChangeCompleted: () -> Unit,
    viewModel: PasswordChangeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PasswordChangeEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                PasswordChangeEvent.PasswordChangeCompleted -> {
                    Toast.makeText(context, "비밀번호를 변경했어요.", Toast.LENGTH_SHORT).show()
                    onPasswordChangeCompleted()
                }
            }
        }
    }

    PasswordChangeScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onCurrentPasswordChange = viewModel::updateCurrentPassword,
        onNewPasswordChange = viewModel::updateNewPassword,
        onNewPasswordConfirmChange = viewModel::updateNewPasswordConfirm,
        onCurrentPasswordVisibilityClick = viewModel::toggleCurrentPasswordVisibility,
        onNewPasswordVisibilityClick = viewModel::toggleNewPasswordVisibility,
        onNewPasswordConfirmVisibilityClick = viewModel::toggleNewPasswordConfirmVisibility,
        onResetClick = viewModel::changePassword,
    )
}
