package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.viewmodel.ProfileEditViewModel

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
