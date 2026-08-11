package com.team_daytodo.daytodo.feature.mypage.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.state.FeedbackEvent
import com.team_daytodo.daytodo.feature.mypage.viewmodel.FeedbackViewModel

@Composable
fun FeedbackRoute(
    onBackClick: () -> Unit,
    viewModel: FeedbackViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is FeedbackEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    FeedbackScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onContentChange = viewModel::onContentChange,
        onSubmitClick = viewModel::onSubmitClick,
    )
}
