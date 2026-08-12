package com.team_daytodo.daytodo.feature.today.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.today.model.TodayEvent
import com.team_daytodo.daytodo.feature.today.viewmodel.TodayViewModel

private const val MaxMemoryPhotoSelection = 8

@Composable
fun TodayRoute(
    modifier: Modifier = Modifier,
    onAddPlaceClick: (courseId: Long) -> Unit = {},
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(MaxMemoryPhotoSelection),
        onResult = { uris -> viewModel.selectMemoryPhotos(uris.map { it.toString() }) },
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is TodayEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                TodayEvent.MemoryPhotosSaved -> {
                    Toast.makeText(context, "추억 사진을 저장했어요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    TodayScreen(
        hasCourse = uiState.hasCourse,
        courseName = uiState.courseName,
        members = uiState.members,
        places = uiState.places,
        onCompleteCourseClick = viewModel::completeCourse,
        onAddPlaceClick = { uiState.courseId?.let(onAddPlaceClick) },
        onDeletePlaceClick = viewModel::deleteCoursePlace,
        onReorderDragStart = viewModel::onReorderDragStart,
        onReorderCommit = viewModel::commitReorder,
        selectedMemoryPhotoUris = uiState.selectedMemoryPhotoUris,
        isSavingMemoryPhotos = uiState.isSavingMemoryPhotos,
        onAddMemoryPhotosClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        onSaveMemoryPhotosClick = { viewModel.saveMemoryPhotos(uiState.selectedMemoryPhotoUris) },
        modifier = modifier,
    )
}
