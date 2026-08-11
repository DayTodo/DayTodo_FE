package com.team_daytodo.daytodo.feature.mypage.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.mypage.state.InterestRegionEvent
import com.team_daytodo.daytodo.feature.mypage.viewmodel.InterestRegionViewModel

@Composable
fun InterestRegionRoute(
    onBackClick: () -> Unit = {},
    onSaved: () -> Unit = {},
    viewModel: InterestRegionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                InterestRegionEvent.Saved -> {
                    Toast.makeText(context, "관심지역을 저장했어요.", Toast.LENGTH_SHORT).show()
                    onSaved()
                }
            }
        }
    }

    InterestRegionScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onGroupSelected = viewModel::onGroupSelected,
        onRegionToggled = viewModel::onRegionToggled,
        onSaveClick = viewModel::onSaveClick,
    )
}
