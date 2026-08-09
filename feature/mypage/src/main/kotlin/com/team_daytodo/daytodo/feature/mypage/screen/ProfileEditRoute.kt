package com.team_daytodo.daytodo.feature.mypage.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditEvent
import com.team_daytodo.daytodo.feature.mypage.viewmodel.ProfileEditViewModel

@Composable
fun ProfileEditRoute(
    onBackClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onProfileSaved: () -> Unit = {},
    viewModel: ProfileEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.onPhotoSelected(uri?.toString()) },
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileEditEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                ProfileEditEvent.ProfileSaved -> {
                    Toast.makeText(context, "프로필을 저장했어요.", Toast.LENGTH_SHORT).show()
                    onProfileSaved()
                }
            }
        }
    }

    ProfileEditScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onChangePhotoClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        onChangePasswordClick = onChangePasswordClick,
        onNicknameChange = viewModel::onNicknameChanged,
        onUnlinkAccountClick = viewModel::onToggleAccountLinkClick,
        onSaveClick = viewModel::onSaveClick,
    )
}
