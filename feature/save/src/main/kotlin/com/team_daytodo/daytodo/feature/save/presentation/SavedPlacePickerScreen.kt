package com.team_daytodo.daytodo.feature.save.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.domain.magazine.model.SavedPlaceSortType
import com.team_daytodo.daytodo.feature.save.model.SavedPlacePickerUiState
import com.team_daytodo.daytodo.feature.save.presentation.component.SaveSortDialog
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceEmptyContent
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceGrid
import com.team_daytodo.daytodo.feature.save.presentation.component.SavedPlaceSortBar
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButtonState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun SavedPlacePickerScreen(
    uiState: SavedPlacePickerUiState,
    onBackClick: () -> Unit,
    onSortClick: () -> Unit,
    onDismissSortDialog: () -> Unit,
    onSortTypeClick: (SavedPlaceSortType) -> Unit,
    onPlaceClick: (String) -> Unit,
    onImportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                SavedPlaceSortBar(
                    sortType = uiState.sortType,
                    onClick = onSortClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (!uiState.isLoading && uiState.places.isEmpty()) {
                    SavedPlaceEmptyContent(
                        message = "저장한 장소가 없어요",
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    SavedPlaceGrid(
                        places = uiState.places,
                        selectedPlaceIds = uiState.selectedPlaceIds,
                        selectionMode = true,
                        onPlaceClick = onPlaceClick,
                        contentPadding = PaddingValues(bottom = 196.dp),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        DayTodoNextStepButton(
            text = "장소 불러오기",
            state = when {
                uiState.isLoading -> DayTodoNextStepButtonState.Loading
                uiState.canImport -> DayTodoNextStepButtonState.Enabled
                else -> DayTodoNextStepButtonState.Disabled
            },
            onClick = onImportClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.5.dp)
                .padding(bottom = 99.dp),
        )
    }

    if (uiState.isSortDialogVisible) {
        SaveSortDialog(
            selectedSortType = uiState.sortType,
            onSortTypeClick = onSortTypeClick,
            onDismissRequest = onDismissSortDialog,
        )
    }
}
