package com.team_daytodo.daytodo.feature.auth

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
import com.team_daytodo.daytodo.feature.auth.model.ProfileSetupEvent
import com.team_daytodo.daytodo.feature.auth.presentation.ProfileSetupScreen

@Composable
fun ProfileSetupRoute(
    onBackClick: () -> Unit,
    onProfileSaved: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.updateProfileImage(uri?.toString()) },
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileSetupEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                ProfileSetupEvent.ProfileSaved -> {
                    Toast.makeText(context, "프로필을 저장했어요.", Toast.LENGTH_SHORT).show()
                    onProfileSaved()
                }
            }
        }
    }

    ProfileSetupScreen(
        uiState = uiState,
        onNicknameChange = viewModel::updateNickname,
        onProfileImageClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        onBackClick = onBackClick,
        onSubmitClick = viewModel::saveProfile,
    )
}
