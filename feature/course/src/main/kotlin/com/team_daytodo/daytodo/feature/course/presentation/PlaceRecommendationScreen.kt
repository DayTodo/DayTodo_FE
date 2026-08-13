package com.team_daytodo.daytodo.feature.course.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.model.PlaceBottomSheetState
import com.team_daytodo.daytodo.feature.course.model.PlaceCourseMode
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationUiState
import com.team_daytodo.daytodo.feature.course.model.RecommenderFilter
import com.team_daytodo.daytodo.feature.course.presentation.component.CoursePlanBottomSheet
import com.team_daytodo.daytodo.feature.course.presentation.component.EmptySearchBottomSheet
import com.team_daytodo.daytodo.feature.course.presentation.component.PlaceCourseModeSwitch
import com.team_daytodo.daytodo.feature.course.presentation.component.PlaceRecommendationMap
import com.team_daytodo.daytodo.feature.course.presentation.component.PlaceRecommendationSearchBar
import com.team_daytodo.daytodo.feature.course.presentation.component.RecommendationBottomSheet
import com.team_daytodo.daytodo.feature.course.presentation.component.SearchResultBottomSheet
import com.team_daytodo.daytodo.feature.course.presentation.component.mapBottomPadding
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun PlaceRecommendationScreen(
    uiState: PlaceRecommendationUiState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit,
    onModeClick: (PlaceCourseMode) -> Unit,
    onRecommenderClick: (RecommenderFilter) -> Unit,
    onMarkerClick: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    onSheetDragHandleClick: () -> Unit,
    onSheetStateChange: (PlaceBottomSheetState) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onRecommendClick: (String) -> Unit,
    onToggleCoursePlaceClick: (String) -> Unit,
    onRemoveCoursePlaceClick: (String) -> Unit,
    onMoveCoursePlace: (Int, Int) -> Unit,
    onSavedPlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        DayTodoHeaderSection(
            title = "장소 추천 & 추가",
            onBackClick = onBackClick,
            rightContent = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "코스설정 수정",
                    tint = DayTodoTheme.colors.textPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(role = Role.Button, onClick = onEditClick)
                        .padding(3.dp),
                )
            },
        )

        Box(modifier = Modifier.fillMaxSize()) {
            PlaceRecommendationMap(
                markers = uiState.displayedMapPlaces,
                selectedPlaceId = uiState.selectedPlaceId,
                regionCoordinate = uiState.course?.regionCoordinate,
                bottomMapPadding = uiState.mapBottomPadding(),
                onMarkerClick = onMarkerClick,
                modifier = Modifier.fillMaxSize(),
            )

            PlaceRecommendationSearchBar(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                onSearch = onSearch,
                onClearClick = onClearSearch,
                onFocusChange = onSearchFocusChange,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp),
            )

            PlaceCourseModeSwitch(
                mode = uiState.mode,
                onModeClick = onModeClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 93.dp, end = 20.dp),
            )

            when {
                uiState.isSearchEmpty -> EmptySearchBottomSheet(
                    sheetState = uiState.sheetState,
                    onHandleClick = onSheetDragHandleClick,
                    onSheetStateChange = onSheetStateChange,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
                uiState.searchResults.isNotEmpty() -> SearchResultBottomSheet(
                    results = uiState.searchResults,
                    sheetState = uiState.sheetState,
                    onHandleClick = onSheetDragHandleClick,
                    onSheetStateChange = onSheetStateChange,
                    onRecommendClick = onRecommendClick,
                    onToggleCoursePlaceClick = onToggleCoursePlaceClick,
                    onPlaceClick = onPlaceClick,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
                uiState.mode == PlaceCourseMode.Course -> CoursePlanBottomSheet(
                    places = uiState.course?.coursePlaces.orEmpty(),
                    sheetState = uiState.sheetState,
                    onHandleClick = onSheetDragHandleClick,
                    onSheetStateChange = onSheetStateChange,
                    onRemovePlaceClick = onRemoveCoursePlaceClick,
                    onMovePlace = onMoveCoursePlace,
                    onPlaceClick = onPlaceClick,
                    onAddPlaceClick = { onModeClick(PlaceCourseMode.Recommendation) },
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
                else -> RecommendationBottomSheet(
                    uiState = uiState,
                    sheetState = uiState.sheetState,
                    onHandleClick = onSheetDragHandleClick,
                    onSheetStateChange = onSheetStateChange,
                    onRecommenderClick = onRecommenderClick,
                    onSavedPlaceClick = onSavedPlaceClick,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick,
                    onToggleCoursePlaceClick = onToggleCoursePlaceClick,
                    onPlaceClick = onPlaceClick,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }
}
