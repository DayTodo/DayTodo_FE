package com.team_daytodo.daytodo.feature.mypage.state

sealed interface MypageDialogState {
    data object None : MypageDialogState

    data object LogoutConfirm : MypageDialogState

    data object LogoutDone : MypageDialogState

    data object WithdrawConfirm : MypageDialogState

    data object WithdrawDone : MypageDialogState
}
