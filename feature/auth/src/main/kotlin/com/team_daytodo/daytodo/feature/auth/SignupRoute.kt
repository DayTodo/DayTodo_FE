package com.team_daytodo.daytodo.feature.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.auth.model.SignupEvent
import com.team_daytodo.daytodo.feature.auth.presentation.SignupScreen

@Composable
fun SignupRoute(
    onBackClick: () -> Unit,
    onSignupCompleted: (needsProfileSetup: Boolean) -> Unit,
    viewModel: SignupViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SignupEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is SignupEvent.SignupCompleted -> {
                    onSignupCompleted(event.needsProfileSetup)
                }
            }
        }
    }

    SignupScreen(
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onPasswordConfirmChange = viewModel::updatePasswordConfirm,
        onTermsAgreementChange = viewModel::updateTermsAgreement,
        onPasswordVisibilityClick = viewModel::togglePasswordVisibility,
        onPasswordConfirmVisibilityClick = viewModel::togglePasswordConfirmVisibility,
        onSignupClick = viewModel::signup,
        onBackClick = onBackClick,
    )
}
