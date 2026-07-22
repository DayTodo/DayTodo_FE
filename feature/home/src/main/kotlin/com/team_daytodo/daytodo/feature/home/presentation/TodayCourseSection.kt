package com.team_daytodo.daytodo.feature.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.model.TodayCourse
import com.team_daytodo.daytodo.feature.home.presentation.component.HomeEmptyStateCard
import com.team_daytodo.daytodo.feature.home.presentation.component.TodayCourseCard
import com.team_daytodo.daytodo.uikit.component.DayTodoSectionTitle

@Composable
internal fun TodayCourseSection(todayCourse: TodayCourse?) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        DayTodoSectionTitle(title = "진행 중인 코스")
        Spacer(modifier = Modifier.height(7.dp))
        if (todayCourse == null) {
            HomeEmptyStateCard(message = "현재 진행중인 코스가 없어요.")
        } else {
            TodayCourseCard(todayCourse = todayCourse)
        }
    }
}
