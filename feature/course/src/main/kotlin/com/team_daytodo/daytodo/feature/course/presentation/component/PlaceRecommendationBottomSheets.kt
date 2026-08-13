package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.feature.course.model.PlaceBottomSheetState
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationUiState
import com.team_daytodo.daytodo.feature.course.model.RecommenderFilter
import com.team_daytodo.daytodo.feature.course.model.displayName
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import com.team_daytodo.daytodo.uikit.R as UIKitR

@Composable
internal fun RecommendationBottomSheet(
    uiState: PlaceRecommendationUiState,
    sheetState: PlaceBottomSheetState,
    onHandleClick: () -> Unit,
    onSheetStateChange: (PlaceBottomSheetState) -> Unit,
    onRecommenderClick: (RecommenderFilter) -> Unit,
    onSavedPlaceClick: () -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onToggleCoursePlaceClick: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        sheetState = sheetState,
        collapsedHeight = 426.dp,
        expandedHeight = 754.dp,
        onHandleClick = onHandleClick,
        onSheetStateChange = onSheetStateChange,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            uiState.recommenderFilters.forEach { filter ->
                RecommenderChip(
                    filter = filter,
                    selected = uiState.selectedRecommender == filter,
                    onClick = { onRecommenderClick(filter) },
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF888888), RoundedCornerShape(12.dp))
                .clickable(role = Role.Button, onClick = onSavedPlaceClick)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "저장된 장소에서 불러오기",
                style = DayTodoTheme.typography.label3.copy(letterSpacing = 0.sp),
                color = Color(0xFF888888),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(
                items = uiState.visibleRecommendations,
                key = { index, item -> "recommendation-$index-${item.place.id}-${item.recommender.displayName()}" },
            ) { index, recommendation ->
                val coursePlaces = uiState.course?.coursePlaces.orEmpty()
                RecommendationPlaceCard(
                    index = index + 1,
                    recommendation = recommendation,
                    currentMemberId = uiState.course?.currentMemberId.orEmpty(),
                    isInCourse = coursePlaces.any { coursePlace ->
                        coursePlace.id == recommendation.place.id ||
                            coursePlace.name == recommendation.place.name
                    },
                    onClick = { onPlaceClick(recommendation.place.id) },
                    onLikeClick = { onLikeClick(recommendation.place.id) },
                    onCommentClick = { onCommentClick(recommendation.place.id) },
                    onToggleCoursePlaceClick = { onToggleCoursePlaceClick(recommendation.place.id) },
                )
            }
        }
    }
}

@Composable
internal fun SearchResultBottomSheet(
    results: List<CoursePlaceSearchResult>,
    sheetState: PlaceBottomSheetState,
    onHandleClick: () -> Unit,
    onSheetStateChange: (PlaceBottomSheetState) -> Unit,
    onRecommendClick: (String) -> Unit,
    onToggleCoursePlaceClick: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        sheetState = sheetState,
        collapsedHeight = 462.dp,
        expandedHeight = 754.dp,
        onHandleClick = onHandleClick,
        onSheetStateChange = onSheetStateChange,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(
                items = results,
                key = { index, item -> "search-$index-${item.place.id}" },
            ) { index, result ->
                SearchPlaceCard(
                    index = index + 1,
                    result = result,
                    onClick = { onPlaceClick(result.place.id) },
                    onRecommendClick = { onRecommendClick(result.place.id) },
                    onToggleCoursePlaceClick = { onToggleCoursePlaceClick(result.place.id) },
                )
            }
        }
    }
}

@Composable
internal fun EmptySearchBottomSheet(
    sheetState: PlaceBottomSheetState,
    onHandleClick: () -> Unit,
    onSheetStateChange: (PlaceBottomSheetState) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        sheetState = sheetState,
        collapsedHeight = 424.dp,
        expandedHeight = 424.dp,
        onHandleClick = onHandleClick,
        onSheetStateChange = onSheetStateChange,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 86.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = UIKitR.drawable.ic_logo),
                contentDescription = null,
                tint = Color(0xFFDFDFDF),
                modifier = Modifier.size(width = 72.dp, height = 118.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "검색된 결과가 없습니다",
                style = DayTodoTheme.typography.headlineLarge.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun RecommenderChip(
    filter: RecommenderFilter,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(30.dp)
            .widthIn(min = 51.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(if (selected) DayTodoTheme.colors.brandPrimary else Color(0xFFE7E7FF))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 11.5.dp, vertical = 4.5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = filter.displayName(),
            style = DayTodoTheme.typography.label2.copy(letterSpacing = 0.sp),
            color = if (selected) Color.White else DayTodoTheme.colors.brandPrimary,
            maxLines = 1,
        )
    }
}
