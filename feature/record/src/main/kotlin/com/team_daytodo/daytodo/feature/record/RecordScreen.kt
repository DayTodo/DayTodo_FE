package com.team_daytodo.daytodo.feature.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.component.RecordCalendarSection
import com.team_daytodo.daytodo.feature.record.component.RecordPhotoRow
import com.team_daytodo.daytodo.feature.record.component.VisitedCourseItem
import com.team_daytodo.daytodo.feature.record.model.RecordUiState
import com.team_daytodo.daytodo.feature.record.model.VisitedCourse
import com.team_daytodo.daytodo.feature.record.model.sampleRecordUiState
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.time.LocalDate

@Composable
fun RecordScreen(
    uiState: RecordUiState,
    onBackClick: () -> Unit = {},
    onDateClick: (LocalDate) -> Unit = {},
    onPreviousMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    // 사진 클릭 시 선택된 날짜의 사진 목록 기준 인덱스를 넘긴다.
    onPhotoClick: (Int) -> Unit = {},
    onMorePhotosClick: () -> Unit = {},
    onSaveCourseClick: (VisitedCourse) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "뒤로가기",
                        )
                    }
                    Text(
                        text = "기록",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            RecordCalendarSection(
                yearMonth = uiState.currentMonth,
                selectedDate = uiState.selectedDate,
                courseDates = uiState.courseDates,
                onDateClick = onDateClick,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
            )

            RecordPhotoRow(
                photos = uiState.selectedPhotos,
                onPhotoClick = onPhotoClick,
                onMoreClick = onMorePhotosClick,
                modifier = Modifier.padding(top = 32.dp),
            )

            Text(
                text = "다녀간 코스",
                style = DayTodoTheme.typography.label2,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp),
            ) {
                uiState.selectedCourses.forEach { course ->
                    VisitedCourseItem(
                        course = course,
                        onSaveClick = onSaveCourseClick,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun RecordScreenPreview() {
    DayTodoTheme {
        RecordScreen(uiState = sampleRecordUiState())
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun RecordScreenEmptyPhotoPreview() {
    DayTodoTheme {
        // 5/19: 코스는 있으나 사진 없음
        RecordScreen(
            uiState = sampleRecordUiState().copy(selectedDate = LocalDate.of(2026, 5, 19)),
        )
    }
}
