package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.team_daytodo.daytodo.uikit.component.DayTodoCircleAddIcon
import com.team_daytodo.daytodo.uikit.component.DayTodoFloatingActionMenu
import com.team_daytodo.daytodo.uikit.component.DayTodoFloatingMenuItem
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

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
            DayTodoCircleAddIcon(
                containerColor = HomeCollapsedFabColor,
            )
        },
        expandedFabContent = {
            DayTodoCircleAddIcon(
                containerColor = DayTodoTheme.colors.iconDisabled,
            )
        },
    )
}

private val HomeCollapsedFabColor = Color(0xFF8B8AF5)

@Preview
@Composable
fun PreviewHomeFloatingActionButton() {
    HomeFloatingActionButton(
        onCreateCourseClick = {},
        onJoinCourseClick = {},
    )
}
