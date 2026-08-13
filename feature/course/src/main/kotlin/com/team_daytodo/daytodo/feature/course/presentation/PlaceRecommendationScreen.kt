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
import com.team_daytodo.daytodo.domain.course.model.CourseDate
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
import java.time.LocalDate

@Composable
fun PlaceRecommendationScreen(
    uiState: PlaceRecommendationUiState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onEditDisabledClick: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit,
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
        // PLN-001(코스설정 수정)은 당일 코스에는 BE(CourseService.validateSameDayEditNotAllowed)가
        // 항상 400(COURSE_SAME_DAY_EDIT_NOT_ALLOWED)으로 거부한다. 이 화면은 TDY-005(당일 장소추가,
        // 제한 없음)와 화면을 공유하고 있어, 아이콘이 평소와 똑같이 눌려서 사용자가 "장소추가가
        // 안 된다"고 오인하기 쉬웠다. 당일이면 아이콘을 흐리게 표시하고 탭해도 편집 화면으로
        // 보내지 않는다.
        val isCourseToday = uiState.course?.date?.isToday() ?: false
        DayTodoHeaderSection(
            title = "장소 추천 & 추가",
            onBackClick = onBackClick,
            rightContent = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = if (isCourseToday) {
                        "코스설정 수정 (당일에는 수정할 수 없어요)"
                    } else {
                        "코스설정 수정"
                    },
                    tint = if (isCourseToday) DayTodoTheme.colors.iconDisabled else DayTodoTheme.colors.textPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(role = Role.Button) {
                            if (isCourseToday) onEditDisabledClick() else onEditClick()
                        }
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

private fun CourseDate.isToday(): Boolean {
    val today = LocalDate.now()
    return year == today.year && month == today.monthValue && day == today.dayOfMonth
}
