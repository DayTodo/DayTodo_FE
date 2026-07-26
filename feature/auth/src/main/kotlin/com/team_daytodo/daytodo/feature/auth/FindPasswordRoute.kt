package com.team_daytodo.daytodo.feature.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.auth.model.FindPasswordEvent
import com.team_daytodo.daytodo.feature.auth.presentation.FindPasswordScreen

@Composable
fun FindPasswordRoute(
    onBackClick: () -> Unit,
    onVerificationCompleted: (verificationToken: String) -> Unit,
    viewModel: FindPasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is FindPasswordEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is FindPasswordEvent.VerificationCompleted -> {
                    Toast.makeText(context, "인증이 완료됐어요.", Toast.LENGTH_SHORT).show()
                    onVerificationCompleted(event.verificationToken)
                }
            }
        }
    }

    FindPasswordScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEmailChange = viewModel::updateEmail,
        onVerificationCodeChange = viewModel::updateVerificationCode,
        onSendCodeClick = viewModel::sendVerificationCode,
        onFindIdClick = {
            Toast.makeText(context, "아직 구현 X", Toast.LENGTH_SHORT).show()
        },
        onConfirmClick = viewModel::verifyCode,
    )
}
