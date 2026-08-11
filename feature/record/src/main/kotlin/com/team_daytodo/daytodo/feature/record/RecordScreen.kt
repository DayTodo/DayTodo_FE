package com.team_daytodo.daytodo.feature.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.component.RecordCalendarSection
import com.team_daytodo.daytodo.feature.record.component.RecordPhotoRow
import com.team_daytodo.daytodo.feature.record.component.RecordPlaceItem
import com.team_daytodo.daytodo.feature.record.component.VisitedCourseItem
import com.team_daytodo.daytodo.feature.record.model.RecordUiState
import com.team_daytodo.daytodo.feature.record.model.sampleRecordUiState
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.time.LocalDate
import java.time.YearMonth

// 뒤로가기 버튼이 없어도(onBackClick == null) 메뉴바 높이가 줄어들지 않도록
// MypageTopBar와 동일한 최소 높이를 유지한다.
private val TopBarMinHeight = 48.dp

@Composable
fun RecordScreen(
    uiState: RecordUiState,
    onBackClick: (() -> Unit)? = null,
    onDateClick: (LocalDate) -> Unit = {},
    onPreviousMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    onCourseSelect: (Long) -> Unit = {},
    onPhotoClick: (Int) -> Unit = {},
    onMorePhotosClick: () -> Unit = {},
    onSavePlaceClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = TopBarMinHeight),
                ) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterStart),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "뒤로가기",
                            )
                        }
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
            // 코스가 있는 날짜 중 오늘 이하인 날짜만 클릭 가능(미래 날짜는 코스가 있어도 비활성).
            val clickableDates = remember(uiState.coursesByDate) {
                val today = LocalDate.now()
                uiState.coursesByDate
                    .filterValues { it.isNotEmpty() }
                    .keys
                    .filter { !it.isAfter(today) }
                    .toSet()
            }

            RecordCalendarSection(
                yearMonth = YearMonth.of(uiState.currentYear, uiState.currentMonth),
                selectedDate = uiState.selectedDate,
                courseDates = clickableDates,
                onDateClick = onDateClick,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
            )

            val coursesOnSelectedDate = uiState.coursesByDate[uiState.selectedDate].orEmpty()
            val isChoosingCourse = coursesOnSelectedDate.size > 1 && uiState.selectedCourseId == null

            Text(
                text = if (isChoosingCourse) "코스를 선택해주세요" else "다녀간 코스",
                style = DayTodoTheme.typography.label2,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp),
            ) {
                if (isChoosingCourse) {
                    coursesOnSelectedDate.forEach { course ->
                        VisitedCourseItem(
                            title = course.courseName,
                            onClick = { onCourseSelect(course.courseId) },
                        )
                    }
                } else {
                    uiState.places.forEach { place ->
                        RecordPlaceItem(
                            placeName = place.placeName,
                            isSaved = place.placeId in uiState.savedPlaceIds,
                            onSaveClick = { onSavePlaceClick(place.placeId) },
                        )
                    }
                }
            }

            if (uiState.selectedCourseId != null) {
                RecordPhotoRow(
                    photos = uiState.photos,
                    onPhotoClick = onPhotoClick,
                    onMoreClick = onMorePhotosClick,
                )
            }

            Spacer(modifier = Modifier.height(150.dp))
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
private fun RecordScreenMultiCoursePreview() {
    DayTodoTheme {
        // 5/12: 코스가 2개라 선택 UI가 노출되는 상태
        RecordScreen(
            uiState = sampleRecordUiState().copy(
                selectedDate = LocalDate.of(2026, 5, 12),
                selectedCourseId = null,
                photos = emptyList(),
            ),
        )
    }
}
