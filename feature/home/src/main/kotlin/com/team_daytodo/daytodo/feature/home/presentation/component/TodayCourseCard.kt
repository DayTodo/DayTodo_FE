package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.R
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.feature.home.model.CourseMember
import com.team_daytodo.daytodo.feature.home.model.TodayCourse
import com.team_daytodo.daytodo.feature.home.presentation.accentColor
import com.team_daytodo.daytodo.feature.home.presentation.courseCompanionLabel
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun TodayCourseCard(todayCourse: TodayCourse) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(99.dp),
        shape = RoundedCornerShape(12.dp),
        color = todayCourse.relationship.accentColor(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 10.5.dp, end = 20.dp, bottom = 12.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.TopStart),
                text = "${todayCourse.title} 코스를 함께하는 ${todayCourse.relationship.courseCompanionLabel}",
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(end = 104.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                todayCourse.members.forEach { member ->
                    CourseMemberProfile(member = member)
                }
            }
            Text(
                modifier = Modifier.align(Alignment.BottomEnd),
                text = todayCourse.date,
                style = DayTodoTheme.typography.title3,
                color = DayTodoTheme.colors.textSecondary,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTodayCourseCard() {
    TodayCourseCard(
        todayCourse = TodayCourse(
            date = "2026.7.9. (목)",
            title = "성수 감성 산책",
            relationship = Relationship.FRIEND,
            members = listOf(
                CourseMember(name = "민지", profileImage = R.drawable.ic_symbol),
                CourseMember(name = "서연", profileImage = R.drawable.ic_symbol),
                CourseMember(name = "하윤", profileImage = R.drawable.ic_symbol),
            ),
        ),
    )
}
