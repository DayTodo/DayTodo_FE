package com.team_daytodo.daytodo.feature.mypage.state

/**
 * 마이페이지에 떠 있는 다이얼로그 상태.
 *
 * 로그아웃/탈퇴는 각각 경고(1단계) → 완료 안내(2단계) 2단계로 진행되며,
 * 동시에 두 개가 뜨는 경우는 없어 하나의 상태 값으로 표현한다.
 */
sealed interface MypageDialogState {
    /** 떠 있는 다이얼로그 없음. */
    data object None : MypageDialogState

    /** 로그아웃 1단계 — 재로그인이 필요하다는 경고. */
    data object LogoutConfirm : MypageDialogState

    /** 로그아웃 2단계 — 완료 안내. */
    data object LogoutDone : MypageDialogState

    /** 탈퇴 1단계 — 계정 정보가 영구 삭제된다는 경고. */
    data object WithdrawConfirm : MypageDialogState

    /** 탈퇴 2단계 — 완료 안내. */
    data object WithdrawDone : MypageDialogState
}
