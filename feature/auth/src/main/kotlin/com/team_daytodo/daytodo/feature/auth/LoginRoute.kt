package com.team_daytodo.daytodo.feature.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.auth.model.LoginEvent
import com.team_daytodo.daytodo.feature.auth.presentation.LoginScreen

@Composable
fun LoginRoute(
    onNavigateToSignup: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    onLoginCompleted: (needsProfileSetup: Boolean) -> Unit,
    onNaverLoginClick: (
        onAccessTokenReceived: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) -> Unit = { _, onFailure ->
        onFailure("\ub124\uc774\ubc84 \ub85c\uadf8\uc778 \uc124\uc815\uc744 \ud655\uc778\ud574 \uc8fc\uc138\uc694.")
    },
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is LoginEvent.LoginCompleted -> {
                    onLoginCompleted(event.needsProfileSetup)
                }
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onKeepLoggedInChange = viewModel::updateKeepLoggedIn,
        onPasswordVisibilityClick = viewModel::togglePasswordVisibility,
        onSignupClick = onNavigateToSignup,
        onFindPasswordClick = onNavigateToFindPassword,
        onNaverLoginClick = {
            onNaverLoginClick(
                { token -> viewModel.loginWithNaverToken(token) },
                viewModel::showNaverLoginFailure,
            )
        },
        onLoginClick = viewModel::login,
    )
}
