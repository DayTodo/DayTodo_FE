package com.team_daytodo.daytodo.feature.course

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.course.model.CourseListEvent
import com.team_daytodo.daytodo.feature.course.presentation.CourseListScreen

@Composable
fun CourseListRoute(
    onBackClick: () -> Unit,
    onCourseClick: (String) -> Unit,
    viewModel: CourseListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CourseListEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CourseListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onDateSelected = viewModel::selectDate,
        onCourseClick = onCourseClick,
    )
}
