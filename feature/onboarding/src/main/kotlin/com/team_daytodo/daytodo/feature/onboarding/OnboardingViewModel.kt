package com.team_daytodo.daytodo.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.onboarding.usecase.CompleteOnboardingUseCase
import com.team_daytodo.daytodo.domain.onboarding.usecase.GetOnboardingPagesUseCase
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingEvent
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOnboardingPagesUseCase: GetOnboardingPagesUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<OnboardingEvent>()
    val event: SharedFlow<OnboardingEvent> = _event.asSharedFlow()

    init {
        loadOnboardingPages()
    }

    fun moveToNextPage() {
        val currentState = _uiState.value
        if (currentState.isCompleting || currentState.isLoading) return

        if (currentState.isLastPage) {
            completeOnboarding()
            return
        }

        _uiState.update {
            it.copy(
                currentPageIndex = (it.currentPageIndex + 1)
                    .coerceAtMost(it.pages.lastIndex),
            )
        }
    }

    fun skipOnboarding() {
        completeOnboarding()
    }

    fun startDayTodo() {
        completeOnboarding()
    }

    private fun loadOnboardingPages() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            getOnboardingPagesUseCase()
                .onSuccess { pages ->
                    _uiState.update {
                        it.copy(
                            pages = pages,
                            currentPageIndex = 0,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { cause ->
                    val message = cause.userFacingMessage(DefaultLoadFailureMessage)
                    _uiState.update {
                        it.copy(
                            pages = emptyList(),
                            currentPageIndex = 0,
                            isLoading = false,
                            errorMessage = message,
                        )
                    }
                    _event.emit(OnboardingEvent.ShowMessage(message))
                }
        }
    }

    private fun completeOnboarding() {
        if (_uiState.value.isCompleting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCompleting = true) }
            completeOnboardingUseCase()
                .onSuccess {
                    _event.emit(OnboardingEvent.Completed)
                }
                .onFailure { cause ->
                    _event.emit(
                        OnboardingEvent.ShowMessage(
                            cause.userFacingMessage(DefaultCompleteFailureMessage),
                        ),
                    )
                }
            _uiState.update { it.copy(isCompleting = false) }
        }
    }

    private companion object {
        const val DefaultLoadFailureMessage = "온보딩 정보를 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
        const val DefaultCompleteFailureMessage = "온보딩을 완료하지 못했어요. 잠시 후 다시 시도해 주세요."
    }
}

private fun Throwable.userFacingMessage(defaultMessage: String): String =
    message?.takeIf(String::isNotBlank) ?: defaultMessage
