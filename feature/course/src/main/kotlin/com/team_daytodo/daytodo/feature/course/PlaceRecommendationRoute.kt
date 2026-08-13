package com.team_daytodo.daytodo.feature.course

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationEvent
import com.team_daytodo.daytodo.feature.course.presentation.PlaceRecommendationScreen

@Composable
fun PlaceRecommendationRoute(
    courseId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onCommentClick: (String, String) -> Unit,
    onSavedPlaceClick: () -> Unit = {},
    importedSavedPlaces: Boolean = false,
    onImportedSavedPlacesHandled: () -> Unit = {},
    viewModel: PlaceRecommendationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(courseId) {
        viewModel.loadCourse(courseId)
    }

    LaunchedEffect(importedSavedPlaces) {
        if (importedSavedPlaces) {
            viewModel.selectCurrentMemberRecommendations()
            onImportedSavedPlacesHandled()
        }
    }

    DisposableEffect(lifecycleOwner, courseId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshCourse(courseId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is PlaceRecommendationEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    PlaceRecommendationScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEditClick = { onEditClick(courseId) },
        onEditDisabledClick = {
            Toast.makeText(context, "당일에는 코스 설정을 수정할 수 없어요.", Toast.LENGTH_SHORT).show()
        },
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSearch = viewModel::searchPlaces,
        onClearSearch = viewModel::clearSearch,
        onModeClick = viewModel::selectMode,
        onRecommenderClick = viewModel::selectRecommender,
        onMarkerClick = viewModel::selectPlace,
        onPlaceClick = viewModel::selectPlace,
        onSheetDragHandleClick = viewModel::toggleSheetExpanded,
        onSheetStateChange = viewModel::updateSheetState,
        onLikeClick = viewModel::toggleLike,
        onCommentClick = { placeId -> onCommentClick(courseId, placeId) },
        onRecommendClick = viewModel::recommendPlace,
        onToggleCoursePlaceClick = viewModel::toggleCoursePlace,
        onRemoveCoursePlaceClick = viewModel::removeCoursePlace,
        onMoveCoursePlace = viewModel::moveCoursePlace,
        onSavedPlaceClick = onSavedPlaceClick,
    )
}
