package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.viewmodel.ProfileEditViewModel

/**
 * ProfileEditScreen 에 ViewModel 상태를 주입하는 라우트 레이어.
 * UiState 관찰은 이 레벨에서만 하고, 화면은 stateless 로 유지한다.
 * (home 모듈의 HomeRoute 와 동일한 패턴)
 */
@Composable
fun ProfileEditRoute(
    onBackClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onChangePhoneClick: () -> Unit = {},
    onUnlinkAccountClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    viewModel: ProfileEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileEditScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onChangePhotoClick = onChangePhotoClick,
        onChangePasswordClick = onChangePasswordClick,
        onChangePhoneClick = onChangePhoneClick,
        onUnlinkAccountClick = onUnlinkAccountClick,
        onSaveClick = onSaveClick,
    )
}
