package com.team_daytodo.daytodo.feature.record.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.team_daytodo.daytodo.feature.record.RecordScreen
import com.team_daytodo.daytodo.feature.record.RecordViewModel
import com.team_daytodo.daytodo.feature.record.screen.MemoScreen
import com.team_daytodo.daytodo.feature.record.screen.PhotoSelectScreen

internal object RecordRoute {
    const val Record = "record/main"
    const val PhotoSelect = "record/photos"

    const val PhotoIndexArg = "photoIndex"

    const val Memo = "record/memo/{$PhotoIndexArg}"

    fun memo(photoIndex: Int): String = "record/memo/$photoIndex"
}

@Composable
fun RecordNavHost(
    onExit: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = RecordRoute.Record,
        modifier = modifier,
    ) {
        composable(RecordRoute.Record) {
            RecordScreen(
                uiState = uiState,
                onBackClick = onExit,
                onDateClick = viewModel::selectDate,
                onPreviousMonth = viewModel::showPreviousMonth,
                onNextMonth = viewModel::showNextMonth,
                onPhotoClick = { photoIndex ->
                    navController.navigate(RecordRoute.memo(photoIndex))
                },
                onMorePhotosClick = { navController.navigate(RecordRoute.PhotoSelect) },
            )
        }

        composable(RecordRoute.PhotoSelect) {
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
}
