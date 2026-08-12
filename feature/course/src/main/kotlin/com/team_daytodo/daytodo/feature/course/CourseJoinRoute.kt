package com.team_daytodo.daytodo.feature.course

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CourseJoinRoute(
    onBackClick: () -> Unit,
    onJoinCompleted: () -> Unit,
    viewModel: CourseJoinViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                CourseJoinEvent.Joined -> onJoinCompleted()
                is CourseJoinEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    InviteCodeJoinScreen(
        onBackClick = onBackClick,
        onEnterClick = viewModel::joinCourse,
    )
}
