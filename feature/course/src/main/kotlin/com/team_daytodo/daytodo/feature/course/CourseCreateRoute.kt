package com.team_daytodo.daytodo.feature.course

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team_daytodo.daytodo.feature.course.model.CourseCreateEvent
import com.team_daytodo.daytodo.feature.course.model.CourseCreatePhase
import com.team_daytodo.daytodo.feature.course.model.CourseCreateStep
import com.team_daytodo.daytodo.feature.course.presentation.CourseCreateScreen

@Composable
fun CourseCreateRoute(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    viewModel: CourseCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    fun handleBackClick() {
        when (uiState.phase) {
            CourseCreatePhase.Input -> {
                if (uiState.currentStep == CourseCreateStep.RoomName) {
                    onBackClick()
                } else {
                    viewModel.moveToPreviousStep()
                }
            }

            CourseCreatePhase.Loading -> Unit
            CourseCreatePhase.Complete -> onDoneClick()
        }
    }

    BackHandler(
        enabled = uiState.phase != CourseCreatePhase.Input ||
            uiState.currentStep != CourseCreateStep.RoomName,
        onBack = ::handleBackClick,
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CourseCreateEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CourseCreateScreen(
        uiState = uiState,
        onRoomNameChange = viewModel::updateRoomName,
        onRegionSelected = viewModel::selectRegion,
        onDateSelected = viewModel::selectDate,
        onMinBudgetChange = viewModel::updateMinBudget,
        onMaxBudgetChange = viewModel::updateMaxBudget,
        onRelationshipSelected = viewModel::selectRelationship,
        onRetryRegionOptionsClick = viewModel::retryLoadRegionOptions,
        onPastDateClick = {
            Toast.makeText(context, "오늘 이전 날짜는 선택할 수 없어요.", Toast.LENGTH_SHORT).show()
        },
        onBackClick = ::handleBackClick,
        onNextClick = viewModel::moveToNextStep,
        onSubmitClick = viewModel::createCourseRoom,
        onDoneClick = onDoneClick,
    )
}
