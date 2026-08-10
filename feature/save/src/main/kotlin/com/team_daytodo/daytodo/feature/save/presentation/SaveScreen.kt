package com.team_daytodo.daytodo.feature.save.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.domain.bookmark.model.SavedPlaceSortType
import com.team_daytodo.daytodo.feature.save.model.SaveUiState
import com.team_daytodo.daytodo.feature.save.presentation.component.SaveRegionDialog
import com.team_daytodo.daytodo.feature.save.presentation.component.SaveSortDialog
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceEmptyContent
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceGrid
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceRegionBar
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceSortBar
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun SaveScreen(
    uiState: SaveUiState,
    onBackClick: () -> Unit,
    onSortClick: () -> Unit,
    onDismissSortDialog: () -> Unit,
    onSortTypeClick: (SavedPlaceSortType) -> Unit,
    onRegionClick: () -> Unit,
    onDismissRegionDialog: () -> Unit,
    onRegionSelect: (Long?) -> Unit,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        DayTodoHeaderSection(
            title = "저장",
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                SavedPlaceRegionBar(
                    selectedRegionName = uiState.selectedRegionName,
                    onClick = onRegionClick,
                    modifier = Modifier.weight(1f),
                )
                SavedPlaceSortBar(
                    sortType = uiState.sortType,
                    onClick = onSortClick,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (!uiState.isLoading && uiState.places.isEmpty()) {
                SavedPlaceEmptyContent(
                    message = "저장한 장소가 없어요",
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                SavedPlaceGrid(
                    places = uiState.places,
                    onPlaceClick = onPlaceClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    if (uiState.isSortDialogVisible) {
        SaveSortDialog(
            selectedSortType = uiState.sortType,
            onSortTypeClick = onSortTypeClick,
            onDismissRequest = onDismissSortDialog,
        )
    }

    if (uiState.isRegionDialogVisible) {
        SaveRegionDialog(
            regions = uiState.regions,
            selectedRegionId = uiState.selectedRegionId,
            onRegionClick = onRegionSelect,
            onDismissRequest = onDismissRegionDialog,
        )
    }
}
