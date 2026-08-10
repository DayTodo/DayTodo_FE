package com.team_daytodo.daytodo.feature.course

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.course.model.PlaceCommentEvent
import com.team_daytodo.daytodo.feature.course.presentation.PlaceCommentScreen

@Composable
fun PlaceCommentRoute(
    courseId: String,
    placeId: String,
    onBackClick: () -> Unit,
    viewModel: PlaceCommentViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(courseId, placeId) {
        viewModel.loadComments(
            courseId = courseId,
            placeId = placeId,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PlaceCommentEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    PlaceCommentScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onInputChange = viewModel::updateInput,
        onSubmitClick = viewModel::submitComment,
    )
}
