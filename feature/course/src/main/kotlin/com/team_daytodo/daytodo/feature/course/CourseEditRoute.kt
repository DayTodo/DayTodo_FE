package com.team_daytodo.daytodo.feature.course

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.course.model.CourseEditEvent
import com.team_daytodo.daytodo.feature.course.presentation.CourseEditScreen

@Composable
fun CourseEditRoute(
    courseId: String,
    onBackClick: () -> Unit,
    viewModel: CourseEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(courseId) {
        viewModel.loadCourse(courseId)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CourseEditEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                CourseEditEvent.Saved -> {
                    Toast.makeText(context, "코스 설정을 수정했어요.", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
            }
        }
    }

    CourseEditScreen(
        uiState = uiState,
        onNameChange = viewModel::updateName,
        onRegionSelected = viewModel::selectRegion,
        onDateSelected = viewModel::selectDate,
        onMinBudgetChange = viewModel::updateMinBudget,
        onMaxBudgetChange = viewModel::updateMaxBudget,
        onRetryRegionOptionsClick = viewModel::retryLoadRegionOptions,
        onBackClick = onBackClick,
        onShareClick = {
            Toast.makeText(context, "초대 링크 공유는 API 연결 시 연동할게요.", Toast.LENGTH_SHORT).show()
        },
        onSubmitClick = viewModel::save,
    )
}
