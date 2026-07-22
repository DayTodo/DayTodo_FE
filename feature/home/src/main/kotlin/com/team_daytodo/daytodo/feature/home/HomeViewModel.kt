package com.team_daytodo.daytodo.feature.home

import androidx.lifecycle.ViewModel
import com.team_daytodo.daytodo.feature.home.model.HomeUiState
import com.team_daytodo.daytodo.feature.home.model.sampleHomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(sampleHomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
