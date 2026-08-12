package com.team_daytodo.daytodo.feature.record.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team_daytodo.daytodo.feature.record.RecordScreen
import com.team_daytodo.daytodo.feature.record.RecordViewModel
import com.team_daytodo.daytodo.feature.record.screen.MemoScreen
import com.team_daytodo.daytodo.feature.record.screen.PhotoSelectScreen
import java.time.YearMonth

object RecordRoute {
    const val Record = "record"
    const val PhotoSelect = "record/photos"

    const val PhotoIndexArg = "photoIndex"
    const val Memo = "record/memo/{$PhotoIndexArg}"

    fun memo(photoIndex: Int): String = "record/memo/$photoIndex"
}

fun NavGraphBuilder.recordNavGraph(navController: NavController) {
    composable(RecordRoute.Record) {
        val viewModel: RecordViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.onMonthChanged(YearMonth.of(uiState.currentYear, uiState.currentMonth))
        }

        RecordScreen(
            uiState = uiState,
            onDateClick = viewModel::onDateSelected,
            onPreviousMonth = viewModel::showPreviousMonth,
            onNextMonth = viewModel::showNextMonth,
            onCourseSelect = viewModel::onCourseSelected,
            onPhotoClick = { photoIndex ->
                navController.navigate(RecordRoute.memo(photoIndex))
            },
            onMorePhotosClick = { navController.navigate(RecordRoute.PhotoSelect) },
            onSavePlaceClick = viewModel::onSavePlaceClick,
        )
    }

    composable(RecordRoute.PhotoSelect) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(RecordRoute.Record)
        }
        val viewModel: RecordViewModel = hiltViewModel(viewModelStoreOwner = parentEntry)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        PhotoSelectScreen(
            photos = uiState.photos,
            onBackClick = { navController.popBackStack() },
            onPhotoClick = { photoIndex ->
                navController.navigate(RecordRoute.memo(photoIndex))
            },
        )
    }

    composable(
        route = RecordRoute.Memo,
        arguments = listOf(
            navArgument(RecordRoute.PhotoIndexArg) { type = NavType.IntType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(RecordRoute.Record)
        }
        val viewModel: RecordViewModel = hiltViewModel(viewModelStoreOwner = parentEntry)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val photoIndex = backStackEntry.arguments
            ?.getInt(RecordRoute.PhotoIndexArg)
            ?: 0

        MemoScreen(
            photos = uiState.photos,
            diaryContent = uiState.diaryContent,
            initialPhotoIndex = photoIndex,
            onBackClick = { navController.popBackStack() },
            onDiaryContentChange = viewModel::onDiaryContentChanged,
            onSaveDiaryClick = viewModel::onSaveDiaryClick,
        )
    }
}
