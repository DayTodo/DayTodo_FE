package com.team_daytodo.daytodo.feature.mypage.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team_daytodo.daytodo.feature.mypage.R
import com.team_daytodo.daytodo.feature.mypage.state.MypageDialogState
import com.team_daytodo.daytodo.uikit.dialog.DayTodoAlertDialog
import com.team_daytodo.daytodo.uikit.dialog.DayTodoMessageDialog

@Composable
internal fun MypageDialogHost(
    state: MypageDialogState,
    onStateChange: (MypageDialogState) -> Unit,
    onLogoutConfirmClick: () -> Unit,
    onWithdrawConfirmClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val dismiss = { onStateChange(MypageDialogState.None) }
    val finish = {
        onStateChange(MypageDialogState.None)
        onNavigateToLogin()
    }

    when (state) {
        MypageDialogState.None -> Unit

        MypageDialogState.LogoutConfirm -> DayTodoAlertDialog(
            title = "주의",
            message = stringResource(R.string.mypage_logout_confirm_message),
            onConfirm = onLogoutConfirmClick,
            onDismiss = dismiss,
        )

        MypageDialogState.LogoutDone -> DayTodoMessageDialog(
            message = stringResource(R.string.mypage_logout_done_message),
            onDismiss = finish,
        )

        MypageDialogState.WithdrawConfirm -> DayTodoAlertDialog(
            title = "주의",
            message = stringResource(R.string.mypage_withdraw_confirm_message),
            onConfirm = onWithdrawConfirmClick,
            onDismiss = dismiss,
        )

        MypageDialogState.WithdrawDone -> DayTodoMessageDialog(
            message = stringResource(R.string.mypage_withdraw_done_message),
            onDismiss = finish,
        )
    }
}
