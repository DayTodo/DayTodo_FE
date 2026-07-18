package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.feature.home.model.CreatedCourse
import com.team_daytodo.daytodo.feature.home.presentation.component.CreatedCourseCard
import com.team_daytodo.daytodo.feature.home.presentation.component.HomeEmptyStateCard
import com.team_daytodo.daytodo.uikit.component.DayTodoSectionTitle

@Composable
internal fun CreatedCourseSection(
    courses: List<CreatedCourse>,
    onMoreClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onMoreClick)
    ) {
        CreatedCourseHeader()
        Spacer(modifier = Modifier.height(7.dp))
        if (courses.isEmpty()) {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                HomeEmptyStateCard(message = "아직 생성된 방이 없어요.")
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
            ) {
                items(courses, key = { course -> course.title }) { course ->
                    CreatedCourseCard(course = course)
                }
            }
        }
    }
}

@Composable
private fun CreatedCourseHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DayTodoSectionTitle(title = "생성한 방 보러가기")
    }
}

@Preview
@Composable
fun PreviewCreatedCourseSection() {
    CreatedCourseSection(
        courses = listOf(
            CreatedCourse(
                title = "한강 피크닉",
                date = "2026년 7월 12일 일요일",
                memberCount = 4,
                dDay = "D-3",
                relationship = Relationship.FRIEND,
            ),
            CreatedCourse(
                title = "기념일 디너",
                date = "2026년 7월 18일 토요일",
                memberCount = 2,
                dDay = "D-9",
                relationship = Relationship.LOVER,
            )
        ),
        onMoreClick = {}
    )
}