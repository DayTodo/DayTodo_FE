package com.team_daytodo.daytodo.feature.onboarding

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingEvent
import com.team_daytodo.daytodo.feature.onboarding.presentation.OnboardingScreen

@Composable
fun OnboardingRoute(
    onOnboardingCompleted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                OnboardingEvent.Completed -> onOnboardingCompleted()
                is OnboardingEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    OnboardingScreen(
        uiState = uiState,
        onNextClick = viewModel::moveToNextPage,
        onSkipClick = viewModel::skipOnboarding,
        onStartClick = viewModel::startDayTodo,
    )
}
