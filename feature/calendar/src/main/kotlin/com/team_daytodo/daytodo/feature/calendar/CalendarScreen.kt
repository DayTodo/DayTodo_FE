package com.team_daytodo.daytodo.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.calendar.screen.CalendarScreen as CalendarContent
import com.team_daytodo.daytodo.feature.calendar.viewmodel.CalendarViewModel

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarContent(
        selectedDate = uiState.selectedDate,
        onDateSelected = viewModel::selectDate,
        coursesByDate = uiState.coursesByDate,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onMonthChanged = viewModel::onMonthChanged,
        onRetryClick = viewModel::retryLoad,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}
