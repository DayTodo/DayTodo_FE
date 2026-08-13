package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.model.PlaceBottomSheetState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
internal fun CoursePlanBottomSheet(
    places: List<CoursePlace>,
    sheetState: PlaceBottomSheetState,
    onHandleClick: () -> Unit,
    onSheetStateChange: (PlaceBottomSheetState) -> Unit,
    onRemovePlaceClick: (String) -> Unit,
    onMovePlace: (Int, Int) -> Unit,
    onPlaceClick: (String) -> Unit,
    onAddPlaceClick: () -> Unit,
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
        Text(
            text = "코스 보기",
            modifier = Modifier.padding(start = 29.dp, top = 24.dp),
            style = DayTodoTheme.typography.title1.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(
                items = places,
                key = { index, item -> "course-$index-${item.id}" },
            ) { index, place ->
                CoursePlanItem(
                    index = index,
                    place = place,
                    onMove = { direction ->
                        val target = (index + direction).coerceIn(0, places.lastIndex)
                        if (target != index) {
                            onMovePlace(index, target)
                            true
                        } else {
                            false
                        }
                    },
                    onClick = { onPlaceClick(place.id) },
                    onRemoveClick = { onRemovePlaceClick(place.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 20.dp)
                .height(51.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DayTodoTheme.colors.brandPrimary)
                .clickable(role = Role.Button, onClick = onAddPlaceClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "장소 추가",
                style = DayTodoTheme.typography.label2.copy(letterSpacing = 0.sp),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun CoursePlanItem(
    index: Int,
    place: CoursePlace,
    onMove: (Int) -> Boolean,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val currentOnMove by rememberUpdatedState(onMove)
    val reorderStepPx = with(density) { CoursePlanItemReorderStep.toPx() }
    val maxDragOffsetPx = with(density) { CoursePlanItemMaxDragOffset.toPx() }
    var isDragging by remember { mutableStateOf(false) }
    var dragVisualOffset by remember { mutableFloatStateOf(0f) }
    var dragDistanceSinceMove by remember { mutableFloatStateOf(0f) }
    val liftScale by animateFloatAsState(
        targetValue = if (isDragging) 1.025f else 1f,
        animationSpec = spring(
            dampingRatio = 0.68f,
            stiffness = 520f,
        ),
        label = "coursePlanItemLiftScale",
    )
    val cardElevation by animateDpAsState(
        targetValue = if (isDragging) 10.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = 0.76f,
            stiffness = 500f,
        ),
        label = "coursePlanItemElevation",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(CoursePlanItemHeight)
            .zIndex(if (isDragging) 1f else 0f)
            .offset { IntOffset(0, dragVisualOffset.roundToInt()) }
            .graphicsLayer {
                scaleX = liftScale
                scaleY = liftScale
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .pointerInput(place.id) {
                    detectVerticalDragGestures(
                        onDragStart = {
                            isDragging = true
                            dragVisualOffset = 0f
                            dragDistanceSinceMove = 0f
                        },
                        onDragEnd = {
                            isDragging = false
                            dragVisualOffset = 0f
                            dragDistanceSinceMove = 0f
                        },
                        onDragCancel = {
                            isDragging = false
                            dragVisualOffset = 0f
                            dragDistanceSinceMove = 0f
                        },
                        onVerticalDrag = { _, dragAmount ->
                            dragVisualOffset = (dragVisualOffset + dragAmount)
                                .coerceIn(-maxDragOffsetPx, maxDragOffsetPx)
                            dragDistanceSinceMove += dragAmount

                            while (abs(dragDistanceSinceMove) >= reorderStepPx) {
                                val direction = if (dragDistanceSinceMove > 0f) 1 else -1
                                val moved = currentOnMove(direction)
                                if (!moved) {
                                    dragDistanceSinceMove = 0f
                                    dragVisualOffset = dragVisualOffset
                                        .coerceIn(-reorderStepPx / 2f, reorderStepPx / 2f)
                                    break
                                }

                                dragDistanceSinceMove -= direction * reorderStepPx
                                dragVisualOffset = (dragVisualOffset - direction * reorderStepPx)
                                    .coerceIn(-maxDragOffsetPx, maxDragOffsetPx)
                            }
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_handle),
                contentDescription = "장소 순서 변경",
                tint = DayTodoTheme.colors.iconDefault,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(role = Role.Button, onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = cardElevation,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(DayTodoTheme.colors.brandPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = (index + 1).toString(),
                        style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = place.name,
                        style = DayTodoTheme.typography.title2.copy(letterSpacing = 0.sp),
                        color = DayTodoTheme.colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = place.category,
                        style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                        color = DayTodoTheme.colors.textSecondary,
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "코스에서 삭제",
                    tint = DayTodoTheme.colors.iconDefault,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(role = Role.Button, onClick = onRemoveClick),
                )
            }
        }
    }
}

private val CoursePlanItemHeight = 66.dp
private val CoursePlanItemReorderStep = 52.dp
private val CoursePlanItemMaxDragOffset = 78.dp
