package com.team_daytodo.daytodo.feature.mypage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team_daytodo.daytodo.domain.mypage.usecase.GetPoliciesUseCase
import com.team_daytodo.daytodo.feature.mypage.model.PolicyContent
import com.team_daytodo.daytodo.feature.mypage.model.PolicyDocuments
import com.team_daytodo.daytodo.feature.mypage.model.RemotePolicyField
import com.team_daytodo.daytodo.feature.mypage.navigation.MypageRoute
import com.team_daytodo.daytodo.feature.mypage.state.PolicyDocumentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PolicyDocumentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPoliciesUseCase: GetPoliciesUseCase,
) : ViewModel() {
    private val remoteField: RemotePolicyField

    private val _uiState = MutableStateFlow<PolicyDocumentUiState>(PolicyDocumentUiState.Loading)
    val uiState: StateFlow<PolicyDocumentUiState> = _uiState.asStateFlow()

    init {
        val documentIndex = savedStateHandle.get<Int>(MypageRoute.PolicyDocumentIndexArg) ?: 0
        remoteField = (PolicyDocuments[documentIndex].content as PolicyContent.Remote).field
        loadPolicy()
    }

    private fun loadPolicy() {
        viewModelScope.launch {
            getPoliciesUseCase()
                .onSuccess { policies ->
                    val body = when (remoteField) {
                        RemotePolicyField.TermsOfService -> policies.termsOfService
                        RemotePolicyField.PrivacyPolicy -> policies.privacyPolicy
                    }
                    _uiState.update { PolicyDocumentUiState.Content(body) }
                }
                .onFailure {
                    _uiState.update { PolicyDocumentUiState.Error }
                }
        }
    }
}
