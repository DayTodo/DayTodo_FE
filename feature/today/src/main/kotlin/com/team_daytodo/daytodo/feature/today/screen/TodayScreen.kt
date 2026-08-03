package com.team_daytodo.daytodo.feature.today.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.feature.today.component.CourseMemberRow
import com.team_daytodo.daytodo.feature.today.component.CoursePlaceItem
import com.team_daytodo.daytodo.feature.today.component.TodayEmptyContent
import com.team_daytodo.daytodo.feature.today.model.CourseMember
import com.team_daytodo.daytodo.feature.today.model.CoursePlace
import com.team_daytodo.daytodo.feature.today.model.TodayTab
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun TodayScreen(
    hasCourse: Boolean,
    members: List<CourseMember> = emptyList(),
    places: List<CoursePlace> = emptyList(),
    onBackClick: () -> Unit = {},
    onAddPlaceClick: () -> Unit = {},
    onAddCourseClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    startWithMemoryTab: Boolean = false,
) {
    var selectedTab by remember {
        mutableStateOf(if (startWithMemoryTab) TodayTab.MEMORY else TodayTab.COURSE)
    }

    val coursePlaces = remember(places) { places.toMutableStateList() }

    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val fromIndex = coursePlaces.indexOfFirst { it.id == from.key }
        val toIndex = coursePlaces.indexOfFirst { it.id == to.key }
        if (fromIndex != -1 && toIndex != -1) {
            coursePlaces.add(toIndex, coursePlaces.removeAt(fromIndex))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_ios),
                            contentDescription = "뒤로가기",
                        )
                    }
                    Text(
                        text = if (selectedTab == TodayTab.MEMORY) "추억 저장하기" else "투데이 코스",
                        style = DayTodoTheme.typography.title1,
                        color = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0),
                )
            }
        },

    ) { innerPadding ->
        if (!hasCourse) {
            TodayEmptyContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp),
            )
            return@Scaffold
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 150.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    CourseMemoryToggle(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                    )
                }
            }

            if (selectedTab == TodayTab.COURSE) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = DayTodoTheme.colors.backgroundSecondary)
                            .padding(16.dp),
                    ) {
                        Column {
                            Text(
                                text = "홍대거리 코스 멤버",
                                style = DayTodoTheme.typography.label3,
                                color = DayTodoTheme.colors.textPrimary,
                            )
                            CourseMemberRow(
                                members = members,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "코스",
                        style = DayTodoTheme.typography.label2,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    )
                }

                itemsIndexed(
                    items = coursePlaces,
                    key = { _, place -> place.id },
                ) { index, place ->
                    ReorderableItem(reorderableState, key = place.id) { isDragging ->
                        CoursePlaceItem(
                            order = index + 1,
                            place = place,
                            modifier = if (index == coursePlaces.lastIndex) {
                                Modifier
                            } else {
                                Modifier.padding(bottom = 10.dp)
                            },
                            isDragging = isDragging,
                            dragHandleModifier = Modifier.draggableHandle(),
                            onDeleteClick = { placeId ->
                                coursePlaces.removeAll { it.id == placeId }
                            },
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Row(
                            modifier = Modifier.clickable(onClick = onAddCourseClick),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = com.team_daytodo.daytodo.feature.today.R.drawable.ic_plus,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified,
                            )
                            Text(
                                text = "코스 추가",
                                style = DayTodoTheme.typography.label2,
                                color = DayTodoTheme.colors.brandPrimary,
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 56.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Row(
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = DayTodoTheme.colors.brandPrimary)
                                .clickable(onClick = onAddPlaceClick),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "코스 종료하기",
                                style = DayTodoTheme.typography.label2,
                                color = DayTodoTheme.colors.textQuaternary
                            )
                        }
                    }
                }
            } else {
                item {
                    MemoryPhotoGrid(
                        photoCount = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                    )
                }
            }
        }
    }
}



@Composable
private fun MemoryPhotoGrid(
    photoCount: Int,
    modifier: Modifier = Modifier,
) {
    val columns = 3
    val spacing = 6.dp

    BoxWithConstraints(modifier = modifier) {
        val itemSize = (maxWidth - spacing * (columns - 1)) / columns
        val rowCount = (photoCount + 1 + columns - 1) / columns
        val gridHeight = itemSize * rowCount + spacing * (rowCount - 1)

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .background(color = DayTodoTheme.colors.backgroundSecondary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "사진 추가",
                        tint = DayTodoTheme.colors.iconDefault,
                    )
                }
            }

            items(photoCount) {
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .background(color = DayTodoTheme.colors.backgroundSecondary),
                )
            }
        }
    }
}

@Composable
private fun CourseMemoryToggle(
    selectedTab: TodayTab,
    onTabSelected: (TodayTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                // uikit에 해당 토큰이 없어 하드코딩
                color = Color(0xFFECECFF),
                shape = RoundedCornerShape(24.dp),
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        TodayTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        color = if (selected) DayTodoTheme.colors.brandPrimary else Color.Transparent,
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tab.label,
                    style = DayTodoTheme.typography.label3,
                    color = if (selected) Color.White else DayTodoTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TodayScreenEmptyPreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = false,
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TodayScreenWithCoursePreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            members = listOf(
                CourseMember(id = "1", name = "나"),
                CourseMember(id = "2", name = "수아"),
            ),
            places = listOf(
                CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                CoursePlace(id = "2", name = "서울숲", category = "공원"),
                CoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
            ),
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TodayScreenMemoryTabPreview() {
    DayTodoTheme {
        TodayScreen(
            hasCourse = true,
            startWithMemoryTab = true,
            members = listOf(
                CourseMember(id = "1", name = "나"),
                CourseMember(id = "2", name = "수아"),
            ),
            places = listOf(
                CoursePlace(id = "1", name = "성수 카페거리", category = "카페"),
                CoursePlace(id = "2", name = "서울숲", category = "공원"),
                CoursePlace(id = "3", name = "레스토랑 예약", category = "맛집"),
            ),
        )
    }
}
