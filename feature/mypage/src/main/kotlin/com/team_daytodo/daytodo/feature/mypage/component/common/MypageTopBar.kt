package com.team_daytodo.daytodo.feature.mypage.component.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val TopBarMinHeight = 48.dp

@Composable
fun MypageTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
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
                text = title,
                style = DayTodoTheme.typography.title1,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        MypageDivider()
    }
}
