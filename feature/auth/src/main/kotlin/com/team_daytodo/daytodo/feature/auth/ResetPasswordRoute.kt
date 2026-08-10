package com.team_daytodo.daytodo.feature.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.auth.model.ResetPasswordEvent
import com.team_daytodo.daytodo.feature.auth.presentation.ResetPasswordScreen

@Composable
fun ResetPasswordRoute(
    verificationToken: String,
    onPasswordResetCompleted: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ResetPasswordEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                ResetPasswordEvent.PasswordResetCompleted -> {
                    Toast.makeText(context, "비밀번호를 변경했어요.", Toast.LENGTH_SHORT).show()
                    onPasswordResetCompleted()
                }
            }
        }
    }

    ResetPasswordScreen(
        uiState = uiState,
        onPasswordChange = viewModel::updatePassword,
        onPasswordConfirmChange = viewModel::updatePasswordConfirm,
        onPasswordVisibilityClick = viewModel::togglePasswordVisibility,
        onPasswordConfirmVisibilityClick = viewModel::togglePasswordConfirmVisibility,
        onResetClick = { viewModel.resetPassword(verificationToken) },
    )
}
