package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.team_daytodo.daytodo.feature.home.model.HomeUiState
import com.team_daytodo.daytodo.feature.home.model.HomeMagazineUiModel
import com.team_daytodo.daytodo.feature.home.model.sampleHomeUiState
import com.team_daytodo.daytodo.feature.home.presentation.component.HomeFloatingActionButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToSave: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToCourseList: () -> Unit,
    onNavigateToCourseCreate: () -> Unit,
    onNavigateToCourseJoin: () -> Unit,
    onManageRegionClick: () -> Unit = {},
    onInterestRegionDialogDismiss: () -> Unit = {},
    onInterestRegionGroupSelected: (String) -> Unit = {},
    onInterestRegionSelected: (String) -> Unit = {},
    onInterestRegionSaveClick: () -> Unit = {},
    onMagazineClick: (HomeMagazineUiModel) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DayTodoTheme.colors.backgroundDefault),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 204.dp),
        ) {
            item {
                HomeBanner(
                    uiState = uiState,
                    onBookmarkClick = onNavigateToSave,
                    onCalendarClick = onNavigateToCalendar,
                    onLocationClick = onManageRegionClick,
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                TodayCourseSection(
                    todayCourse = uiState.todayCourse,
                    onTodayCourseClick = {},
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                CreatedCourseSection(
                    courses = uiState.createdCourses,
                    onMoreClick = onNavigateToCourseList,
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                MagazineSection(
                    magazines = uiState.todayPickMagazines,
                    onMagazineClick = onMagazineClick,
                )
            }
            item { Spacer(modifier = Modifier.height(108.dp)) }
        }

        HomeFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 148.dp),
            onCreateCourseClick = onNavigateToCourseCreate,
            onJoinCourseClick = onNavigateToCourseJoin,
        )

        if (uiState.showInterestRegionDialog) {
            HomeInterestRegionDialog(
                uiState = uiState,
                onDismissRequest = onInterestRegionDialogDismiss,
                onGroupSelected = onInterestRegionGroupSelected,
                onRegionSelected = onInterestRegionSelected,
                onSaveClick = onInterestRegionSaveClick,
            )
        }
    }
}

@Composable
private fun HomeInterestRegionDialog(
    uiState: HomeUiState,
    onDismissRequest: () -> Unit,
    onGroupSelected: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .widthIn(max = 348.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .background(DayTodoTheme.colors.brandPrimary)
                            .padding(horizontal = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "관심 지역을 설정해주세요.",
                            style = DayTodoTheme.typography.title2,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "관심 지역을 선택하고 나에게 맞는 코스와 장소를 추천받아보세요.",
                            style = DayTodoTheme.typography.caption2,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 320.dp, max = 354.dp)
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                    ) {
                        LazyColumn(
                            modifier = Modifier.width(58.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(uiState.interestRegionGroups, key = { it.parentName }) { group ->
                                HomeInterestRegionParentItem(
                                    text = group.displayName,
                                    selected = group.parentName == uiState.selectedInterestRegionGroupName,
                                    onClick = { onGroupSelected(group.parentName) },
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(22.dp))
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val children = uiState.selectedInterestRegionGroup?.options.orEmpty()
                            items(children, key = { it.key }) { option ->
                                HomeInterestRegionChildItem(
                                    text = option.displayName,
                                    selected = option.key == uiState.selectedInterestRegionOptionKey,
                                    onClick = { onRegionSelected(option.key) },
                                )
                            }
                        }
                    }
                }
            }
            uiState.interestRegionErrorMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    style = DayTodoTheme.typography.caption2,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .width(108.dp)
                    .height(46.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (uiState.canSaveInterestRegions) {
                            DayTodoTheme.colors.brandPrimary
                        } else {
                            Color(0xFFCFCDF8)
                        },
                    )
                    .clickable(
                        enabled = uiState.canSaveInterestRegions,
                        role = Role.Button,
                        onClick = onSaveClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "확인",
                    style = DayTodoTheme.typography.label2,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun HomeInterestRegionParentItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(58.dp)
            .height(33.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFFE0E0F5) else Color.Transparent)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label1,
            color = if (selected) DayTodoTheme.colors.brandPrimary else DayTodoTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HomeInterestRegionChildItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 13.5.dp),
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label2,
            color = if (selected) DayTodoTheme.colors.brandPrimary else DayTodoTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        uiState = sampleHomeUiState(),
        onNavigateToSave = {},
        onNavigateToCalendar = {},
        onNavigateToCourseList = {},
        onNavigateToCourseCreate = {},
        onNavigateToCourseJoin = {},
        onManageRegionClick = {},
        onInterestRegionDialogDismiss = {},
        onInterestRegionGroupSelected = {},
        onInterestRegionSelected = {},
        onInterestRegionSaveClick = {},
    )
}
