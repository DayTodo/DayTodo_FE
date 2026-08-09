package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.model.PolicyContent
import com.team_daytodo.daytodo.feature.mypage.model.PolicyDocuments
import com.team_daytodo.daytodo.feature.mypage.state.PolicyDocumentUiState
import com.team_daytodo.daytodo.feature.mypage.viewmodel.PolicyDocumentViewModel

@Composable
fun PolicyDocumentRoute(
    documentIndex: Int,
    onBackClick: () -> Unit,
) {
    val document = PolicyDocuments[documentIndex]

    when (val content = document.content) {
        is PolicyContent.Static -> {
            PolicyDocumentScreen(
                title = document.title,
                uiState = PolicyDocumentUiState.Content(content.body),
                onBackClick = onBackClick,
            )
        }

        is PolicyContent.Remote -> {
            val viewModel: PolicyDocumentViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            PolicyDocumentScreen(
                title = document.title,
                uiState = uiState,
                onBackClick = onBackClick,
            )
        }
    }
}
