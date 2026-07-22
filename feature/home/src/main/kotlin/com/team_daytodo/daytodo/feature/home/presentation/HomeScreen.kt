package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                TodayCourseSection(todayCourse = uiState.todayCourse)
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
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 148.dp),
            onCreateCourseClick = onNavigateToCourseCreate,
            onJoinCourseClick = onNavigateToCourseJoin,
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
    )
}
