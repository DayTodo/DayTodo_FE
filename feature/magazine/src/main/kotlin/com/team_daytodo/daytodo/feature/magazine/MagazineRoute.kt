package com.team_daytodo.daytodo.feature.magazine

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.magazine.model.MagazineEvent
import com.team_daytodo.daytodo.feature.magazine.presentation.MagazineDetailScreen

@Composable
fun MagazineRoute(
    placeId: String,
    onBackClick: () -> Unit,
    viewModel: MagazineViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(placeId) {
        viewModel.loadPlace(placeId)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MagazineEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MagazineDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBookmarkClick = viewModel::toggleSaved,
    )
}
