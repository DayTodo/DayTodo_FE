package com.team_daytodo.daytodo.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.auth.usecase.CheckAutoLoginUseCase
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
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
) : ViewModel() {
    private val _event = MutableSharedFlow<OnboardingGateEvent>(replay = 1)
    val event: SharedFlow<OnboardingGateEvent> = _event.asSharedFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            if (!isOnboardingCompletedUseCase().getOrDefault(false)) {
                _event.emit(OnboardingGateEvent.ShowOnboarding)
                return@launch
            }

            val autoLoginResult = checkAutoLoginUseCase().getOrNull()
            if (autoLoginResult?.isLoggedIn == true) {
                _event.emit(OnboardingGateEvent.ShowHome)
            } else {
                _event.emit(OnboardingGateEvent.ShowLogin)
            }
        }
    }
}

sealed interface OnboardingGateEvent {
    data object ShowOnboarding : OnboardingGateEvent

    data object ShowLogin : OnboardingGateEvent

    data object ShowHome : OnboardingGateEvent
}
