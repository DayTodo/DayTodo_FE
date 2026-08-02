package com.team_daytodo.daytodo.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.onboarding.usecase.IsOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingGateViewModel @Inject constructor(
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
) : ViewModel() {
    private val _event = MutableSharedFlow<OnboardingGateEvent>(replay = 1)
    val event: SharedFlow<OnboardingGateEvent> = _event.asSharedFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            val event = isOnboardingCompletedUseCase()
                .getOrDefault(false)
                .toGateEvent()
            _event.emit(event)
        }
    }
}

sealed interface OnboardingGateEvent {
    data object ShowOnboarding : OnboardingGateEvent

    data object SkipOnboarding : OnboardingGateEvent
}

private fun Boolean.toGateEvent(): OnboardingGateEvent =
    if (this) {
        OnboardingGateEvent.SkipOnboarding
    } else {
        OnboardingGateEvent.ShowOnboarding
    }
