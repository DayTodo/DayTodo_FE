package com.team_daytodo.daytodo.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.home.presentation.HomeScreen

@Composable
fun HomeRoute(
    onNavigateToSave: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToCourseList: () -> Unit = {},
    onNavigateToCourseCreate: () -> Unit = {},
    onNavigateToCourseJoin: () -> Unit = {},
    onTodayScheduleChanged: (Boolean) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.hasTodaySchedule) {
        onTodayScheduleChanged(uiState.hasTodaySchedule)
    }

    HomeScreen(
        uiState = uiState,
        onNavigateToSave = onNavigateToSave,
        onNavigateToCalendar = onNavigateToCalendar,
        onNavigateToCourseList = onNavigateToCourseList,
        onNavigateToCourseCreate = onNavigateToCourseCreate,
        onNavigateToCourseJoin = onNavigateToCourseJoin,
    )
}
