package com.team_daytodo.daytodo.feature.mypage.component.phonechange

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageDivider
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CurrentPhoneNumberCard(
    phoneNumber: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MypageDivider()

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "현재 전화번호",
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = phoneNumber,
            style = DayTodoTheme.typography.label1,
            color = DayTodoTheme.colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(16.dp))
        MypageDivider()
    }
}
