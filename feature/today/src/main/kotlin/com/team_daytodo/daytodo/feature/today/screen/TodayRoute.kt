package com.team_daytodo.daytodo.feature.today.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.today.viewmodel.TodayViewModel

@Composable
fun TodayRoute(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TodayScreen(
        hasCourse = uiState.hasCourse,
        members = uiState.members,
        places = uiState.places,
        onCompleteCourseClick = viewModel::completeCourse,
        modifier = modifier,
    )
}
