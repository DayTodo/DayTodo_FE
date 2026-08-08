package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.viewmodel.FeedbackViewModel

@Composable
fun FeedbackRoute(
    onBackClick: () -> Unit,
    viewModel: FeedbackViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FeedbackScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onContentChange = viewModel::onContentChange,
        onSubmitClick = viewModel::onSubmitClick,
    )
}
