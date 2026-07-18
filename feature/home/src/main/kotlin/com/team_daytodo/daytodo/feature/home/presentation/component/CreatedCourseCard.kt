package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.model.CreatedCourse
import com.team_daytodo.daytodo.feature.home.presentation.accentColor
import com.team_daytodo.daytodo.feature.home.presentation.badgeColor
import com.team_daytodo.daytodo.feature.home.presentation.emphasisColor
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun CreatedCourseCard(course: CreatedCourse) {
    Surface(
        modifier = Modifier
            .width(217.dp)
            .height(99.dp),
        shape = RoundedCornerShape(11.dp),
        color = course.relationship.accentColor(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.5.dp),
        ) {
            Text(
                text = course.title,
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = course.date,
                style = DayTodoTheme.typography.title3,
                color = DayTodoTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier
                        .width(44.dp)
                        .height(20.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = course.relationship.badgeColor(),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${course.memberCount}명",
                            style = DayTodoTheme.typography.caption1,
                            color = DayTodoTheme.colors.textPrimary,
                        )
                    }
                }
                Text(
                    text = course.dDay,
                    style = DayTodoTheme.typography.headlineLarge,
                    color = course.relationship.emphasisColor(),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCreatedCourseCard() {
    CreatedCourseCard(
        course = CreatedCourse(
            title = "성수 감성 산책",
            date = "2026.7.9. (목)",
            memberCount = 3,
            dDay = "D-1",
            relationship = com.team_daytodo.daytodo.core.model.Relationship.FRIEND,
        )
    )
}
