package com.team_daytodo.daytodo.feature.mypage.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.state.PasswordChangeEvent
import com.team_daytodo.daytodo.feature.mypage.viewmodel.PasswordChangeViewModel
import com.team_daytodo.daytodo.uikit.dialog.DayTodoMessageDialog

@Composable
fun PasswordChangeRoute(
    onBackClick: () -> Unit,
    onPasswordChangeCompleted: () -> Unit,
    viewModel: PasswordChangeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showCompletedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PasswordChangeEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                // 비밀번호 변경 성공 시 BE가 기존 refresh token을 무효화하므로, 로그아웃/탈퇴 완료와
                // 동일하게 DayTodoMessageDialog로 안내한 뒤 확인을 눌러야 다음 화면으로 넘어가게 한다.
                PasswordChangeEvent.PasswordChangeCompleted -> {
                    showCompletedDialog = true
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

    if (showCompletedDialog) {
        DayTodoMessageDialog(
            message = "비밀번호가 변경되었어요.\n다시 로그인해 주세요.",
            onDismiss = {
                showCompletedDialog = false
                onPasswordChangeCompleted()
            },
        )
    }
}
