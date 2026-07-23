package com.team_daytodo.daytodo.feature.record.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.component.DiaryCalendarSection
import com.team_daytodo.daytodo.feature.record.component.DiaryPhoto
import com.team_daytodo.daytodo.feature.record.component.DiaryPhotoRow
import com.team_daytodo.daytodo.feature.record.component.VisitedCourse
import com.team_daytodo.daytodo.feature.record.component.VisitedCourseItem
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme
import java.time.LocalDate
import java.time.YearMonth

// 더미 데이터: 5월 5/12/19/26 에만 코스 존재 (5/19 는 코스는 있으나 사진 없음 케이스)
private val dummyCourseDates: Set<LocalDate> = setOf(
    LocalDate.of(2026, 5, 5),
    LocalDate.of(2026, 5, 12),
    LocalDate.of(2026, 5, 19),
    LocalDate.of(2026, 5, 26),
)

private val dummyPhotosByDate: Map<LocalDate, List<DiaryPhoto>> = mapOf(
    LocalDate.of(2026, 5, 5) to List(4) { DiaryPhoto(id = "0505-$it") },
    LocalDate.of(2026, 5, 12) to List(3) { DiaryPhoto(id = "0512-$it") },
    LocalDate.of(2026, 5, 19) to emptyList(), // 코스는 있으나 사진 없음
    LocalDate.of(2026, 5, 26) to List(5) { DiaryPhoto(id = "0526-$it") },
)

private val dummyCoursesByDate: Map<LocalDate, List<VisitedCourse>> = mapOf(
    LocalDate.of(2026, 5, 5) to listOf(
        VisitedCourse(id = "0505-1", title = "연남동 브런치 코스"),
    ),
    LocalDate.of(2026, 5, 12) to listOf(
        VisitedCourse(id = "0512-1", title = "한강 피크닉 코스"),
        VisitedCourse(id = "0512-2", title = "망원 카페 코스"),
    ),
    LocalDate.of(2026, 5, 19) to listOf(
        VisitedCourse(id = "0519-1", title = "북촌 한옥 산책"),
    ),
    LocalDate.of(2026, 5, 26) to listOf(
        VisitedCourse(id = "0526-1", title = "성수 데이트 코스"),
        VisitedCourse(id = "0526-2", title = "서울숲 나들이"),
        VisitedCourse(id = "0526-3", title = "건대 맛집 투어"),
        VisitedCourse(id = "0526-4", title = "잠실 야경 코스"),
    ),
)

@Composable
fun DiaryScreen(
    onBackClick: () -> Unit = {},
    onPhotoClick: (DiaryPhoto) -> Unit = {},
    onMorePhotosClick: () -> Unit = {},
    onSaveCourseClick: (VisitedCourse) -> Unit = {},
    modifier: Modifier = Modifier,
    // 코스가 있는 날짜만 선택 가능하므로 기본 선택은 코스 있는 날짜(5/26)로 둔다.
    initialSelectedDate: LocalDate = LocalDate.of(2026, 5, 26),
) {
    var selectedDate by remember { mutableStateOf(initialSelectedDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialSelectedDate)) }

    val selectedPhotos = dummyPhotosByDate[selectedDate].orEmpty()
    val selectedCourses = dummyCoursesByDate[selectedDate].orEmpty()

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
            DiaryCalendarSection(
                yearMonth = currentMonth,
                selectedDate = selectedDate,
                courseDates = dummyCourseDates,
                onDateClick = { selectedDate = it },
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) },
            )

            DiaryPhotoRow(
                photos = selectedPhotos,
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
                selectedCourses.forEach { course ->
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
private fun DiaryScreenPreview() {
    DayTodoTheme {
        DiaryScreen(
            initialSelectedDate = LocalDate.of(2026, 5, 26),
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun DiaryScreenEmptyPhotoPreview() {
    DayTodoTheme {
        DiaryScreen(
            initialSelectedDate = LocalDate.of(2026, 5, 19),
        )
    }
}
