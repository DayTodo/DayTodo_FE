package com.team_daytodo.daytodo.feature.record.navigation

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

        RecordScreen(
            uiState = uiState,
            onDateClick = viewModel::selectDate,
            onPreviousMonth = viewModel::showPreviousMonth,
            onNextMonth = viewModel::showNextMonth,
            onPhotoClick = { photoIndex ->
                navController.navigate(RecordRoute.memo(photoIndex))
            },
            onMorePhotosClick = { navController.navigate(RecordRoute.PhotoSelect) },
        )
    }

    composable(RecordRoute.PhotoSelect) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(RecordRoute.Record)
        }
        val viewModel: RecordViewModel = hiltViewModel(viewModelStoreOwner = parentEntry)
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        PhotoSelectScreen(
            photos = uiState.selectedPhotos,
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
            photos = uiState.selectedPhotos,
            memosByPhotoId = uiState.memosByPhotoId,
            initialPhotoIndex = photoIndex,
            onBackClick = { navController.popBackStack() },
            onSubmitMemo = viewModel::addMemo,
        )
    }
}
