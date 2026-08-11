package com.team_daytodo.daytodo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.magazine.usecase.GetMagazinesUseCase
import com.team_daytodo.daytodo.feature.home.model.HomeUiState
import com.team_daytodo.daytodo.feature.home.model.sampleHomeUiState
import com.team_daytodo.daytodo.feature.home.model.toHomeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMagazinesUseCase: GetMagazinesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(sampleHomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMagazines()
    }

    private fun loadMagazines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isMagazineLoading = true, magazineErrorMessage = null) }
            getMagazinesUseCase()
                .onSuccess { magazines ->
                    _uiState.update {
                        it.copy(
                            todayPickMagazines = magazines.map { magazine -> magazine.toHomeUiModel() },
                            isMagazineLoading = false,
                        )
                    }
                }
                .onFailure { cause ->
                    _uiState.update {
                        it.copy(isMagazineLoading = false, magazineErrorMessage = cause.message)
                    }
                }
        }
    }
}
