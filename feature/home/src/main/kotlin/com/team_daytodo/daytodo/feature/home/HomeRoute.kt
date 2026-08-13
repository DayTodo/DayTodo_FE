package com.team_daytodo.daytodo.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    onMagazineClick: (String) -> Unit = {},
    onTodayScheduleChanged: (Boolean) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hasRequestedInterestRegionSetup by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.hasTodaySchedule) {
        onTodayScheduleChanged(uiState.hasTodaySchedule)
    }

    LaunchedEffect(uiState.shouldRequestInterestRegionSetup) {
        if (uiState.shouldRequestInterestRegionSetup && !hasRequestedInterestRegionSetup) {
            hasRequestedInterestRegionSetup = true
            viewModel.showInterestRegionDialog()
        }
    }

    HomeScreen(
        uiState = uiState,
        onNavigateToSave = onNavigateToSave,
        onNavigateToCalendar = onNavigateToCalendar,
        onNavigateToCourseList = onNavigateToCourseList,
        onNavigateToCourseCreate = onNavigateToCourseCreate,
        onNavigateToCourseJoin = onNavigateToCourseJoin,
        onManageRegionClick = viewModel::showInterestRegionDialog,
        onInterestRegionDialogDismiss = viewModel::dismissInterestRegionDialog,
        onInterestRegionGroupSelected = viewModel::selectInterestRegionGroup,
        onInterestRegionSelected = viewModel::selectInterestRegionOption,
        onInterestRegionSaveClick = viewModel::saveInterestRegions,
        onMagazineClick = { magazine -> onMagazineClick(magazine.placeId) },
    )
}
