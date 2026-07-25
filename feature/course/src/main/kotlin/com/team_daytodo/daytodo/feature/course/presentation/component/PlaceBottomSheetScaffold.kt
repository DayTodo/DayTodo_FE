package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.course.model.PlaceBottomSheetState
import com.team_daytodo.daytodo.feature.course.model.PlaceCourseMode
import com.team_daytodo.daytodo.feature.course.model.PlaceRecommendationUiState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlin.math.roundToInt

@Composable
internal fun BaseBottomSheet(
    sheetState: PlaceBottomSheetState,
    collapsedHeight: Dp,
    expandedHeight: Dp,
    onHandleClick: () -> Unit,
    onSheetStateChange: (PlaceBottomSheetState) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    val targetHeight = sheetState.resolveBottomSheetHeight(
        collapsedHeight = collapsedHeight,
        expandedHeight = expandedHeight,
    )
    val animatedHeight by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = 420f,
        ),
        label = "bottomSheetHeight",
    )
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var dragTotal by remember { mutableFloatStateOf(0f) }
    val dragThreshold = with(density) { 56.dp.toPx() }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .offset { IntOffset(0, dragOffset.coerceAtLeast(0f).roundToInt()) }
            .imePadding(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = DayTodoTheme.colors.backgroundTertiary,
        shadowElevation = 12.dp,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BottomSheetHandleOnlyHeight)
                    .clickable(role = Role.Button, onClick = onHandleClick)
                    .pointerInput(sheetState) {
                        detectVerticalDragGestures(
                            onDragStart = {
                                dragTotal = 0f
                                dragOffset = 0f
                            },
                            onVerticalDrag = { _, dragAmount ->
                                dragTotal += dragAmount
                                dragOffset = dragTotal
                            },
                            onDragCancel = {
                                dragTotal = 0f
                                dragOffset = 0f
                            },
                            onDragEnd = {
                                val nextState = when {
                                    dragTotal < -dragThreshold -> when (sheetState) {
                                        PlaceBottomSheetState.Hidden -> PlaceBottomSheetState.Collapsed
                                        PlaceBottomSheetState.Collapsed -> PlaceBottomSheetState.Expanded
                                        PlaceBottomSheetState.Expanded -> PlaceBottomSheetState.Expanded
                                    }
                                    dragTotal > dragThreshold -> when (sheetState) {
                                        PlaceBottomSheetState.Hidden -> PlaceBottomSheetState.Hidden
                                        PlaceBottomSheetState.Collapsed -> PlaceBottomSheetState.Hidden
                                        PlaceBottomSheetState.Expanded -> PlaceBottomSheetState.Collapsed
                                    }
                                    else -> sheetState
                                }
                                dragTotal = 0f
                                dragOffset = 0f
                                onSheetStateChange(nextState)
                            },
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 108.dp, height = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(androidx.compose.ui.graphics.Color(0xFFB9B9F5)),
                )
            }
            if (sheetState != PlaceBottomSheetState.Hidden) {
                content()
            }
        }
    }
}

internal fun PlaceRecommendationUiState.mapBottomPadding(): Dp =
    when {
        isSearchEmpty -> sheetState.resolveBottomSheetHeight(
            collapsedHeight = 424.dp,
            expandedHeight = 424.dp,
        )
        searchResults.isNotEmpty() -> sheetState.resolveBottomSheetHeight(
            collapsedHeight = 462.dp,
            expandedHeight = 754.dp,
        )
        mode == PlaceCourseMode.Course -> sheetState.resolveBottomSheetHeight(
            collapsedHeight = 426.dp,
            expandedHeight = 754.dp,
        )
        else -> sheetState.resolveBottomSheetHeight(
            collapsedHeight = 426.dp,
            expandedHeight = 754.dp,
        )
    }

private fun PlaceBottomSheetState.resolveBottomSheetHeight(
    collapsedHeight: Dp,
    expandedHeight: Dp,
): Dp =
    when (this) {
        PlaceBottomSheetState.Hidden -> BottomSheetHandleOnlyHeight
        PlaceBottomSheetState.Collapsed -> collapsedHeight
        PlaceBottomSheetState.Expanded -> expandedHeight
    }

private val BottomSheetHandleOnlyHeight = 34.dp
