package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun LinkedAccountSection(
    providerName: String,
    accountId: String,
    onUnlinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "연동 계정",
            style = DayTodoTheme.typography.title2,
            color = DayTodoTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(ProfileFieldShape)
                .border(
                    width = 1.dp,
                    color = DayTodoTheme.colors.divider,
                    shape = ProfileFieldShape,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 13.5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_naver),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = providerName,
                        style = DayTodoTheme.typography.label2,
                        color = DayTodoTheme.colors.textPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = CONNECTED_LABEL,
                            style = DayTodoTheme.typography.caption2,
                            color = DayTodoTheme.colors.textPrimary,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = accountId,
                            style = DayTodoTheme.typography.caption2,
                            color = DayTodoTheme.colors.textPrimary,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                ProfileGreyButton(text = "연동 해제", onClick = onUnlinkClick)
            }
        }
    }
}

private const val CONNECTED_LABEL = "연동됨"
