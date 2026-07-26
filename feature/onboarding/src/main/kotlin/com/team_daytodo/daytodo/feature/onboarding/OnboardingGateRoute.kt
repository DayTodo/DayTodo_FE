package com.team_daytodo.daytodo.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun OnboardingGateRoute(
    onShowOnboarding: () -> Unit,
    onSkipOnboarding: () -> Unit,
    viewModel: OnboardingGateViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                OnboardingGateEvent.ShowOnboarding -> onShowOnboarding()
                OnboardingGateEvent.SkipOnboarding -> onSkipOnboarding()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    )
}
