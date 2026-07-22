package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.R
import com.team_daytodo.daytodo.uikit.component.DayTodoFloatingActionMenu
import com.team_daytodo.daytodo.uikit.component.DayTodoFloatingMenuItem

@Composable
internal fun HomeFloatingActionButton(
    modifier: Modifier = Modifier,
    onCreateCourseClick: () -> Unit,
    onJoinCourseClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    DayTodoFloatingActionMenu(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        menuItems = listOf(
            DayTodoFloatingMenuItem(
                text = "새 코스방 만들기",
                onClick = onCreateCourseClick,
            ),
            DayTodoFloatingMenuItem(
                text = "초대된 그룹 들어가기",
                onClick = onJoinCourseClick,
            ),
        ),
        expandedFabContainerColor = Color.Transparent,
        collapsedFabContent = {
            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(id = R.drawable.ic_fab),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        },
        expandedFabContent = {
            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(id = R.drawable.ic_fab_expanded),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        },
    )
}

@Preview
@Composable
fun PreviewHomeFloatingActionButton() {
    HomeFloatingActionButton(
        onCreateCourseClick = {},
        onJoinCourseClick = {},
    )
}
