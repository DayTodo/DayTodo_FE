package com.team_daytodo.daytodo.feature.save

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.save.model.SavedPlacePickerEvent
import com.team_daytodo.daytodo.feature.save.presentation.SavedPlacePickerScreen

@Composable
fun SavedPlacePickerRoute(
    courseId: String,
    onBackClick: () -> Unit,
    onImportCompleted: () -> Unit,
    viewModel: SavedPlacePickerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                SavedPlacePickerEvent.Imported -> onImportCompleted()
                is SavedPlacePickerEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SavedPlacePickerScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSortClick = viewModel::showSortDialog,
        onDismissSortDialog = viewModel::dismissSortDialog,
        onSortTypeClick = viewModel::selectSortType,
        onPlaceClick = viewModel::togglePlaceSelection,
        onImportClick = { viewModel.importSelectedPlaces(courseId) },
    )
}
