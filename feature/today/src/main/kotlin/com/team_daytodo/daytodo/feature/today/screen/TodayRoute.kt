package com.team_daytodo.daytodo.feature.today.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.team_daytodo.daytodo.feature.today.model.sampleTodayCourseData

/**
 * 투데이 화면 진입점. 데이터 의존성을 today feature 내부에서 관리한다.
 * 실제 데이터 소스(ViewModel)가 준비되면 sampleTodayCourseData() 부분만 교체하면 된다.
 */
@Composable
fun TodayRoute(modifier: Modifier = Modifier) {
    val courseData = remember { sampleTodayCourseData() }

    TodayScreen(
        hasCourse = courseData.hasCourse,
        members = courseData.members,
        places = courseData.places,
        modifier = modifier,
    )
}
