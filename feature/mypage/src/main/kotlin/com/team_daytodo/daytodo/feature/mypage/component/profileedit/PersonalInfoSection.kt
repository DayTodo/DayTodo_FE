package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthTextField
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun PersonalInfoSection(
    name: String,
    nickname: String,
    email: String,
    onNicknameChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ProfileInfoField(label = "이름", value = name)
        Spacer(modifier = Modifier.height(20.dp))
        DayTodoAuthTextField(
            label = "닉네임",
            value = nickname,
            onValueChange = onNicknameChange,
            labelStyle = DayTodoTheme.typography.title2,
            labelSpacing = 4.dp,
            textStyle = DayTodoTheme.typography.caption1,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        ProfileInfoField(
            label = "이메일",
            value = email,
            filled = true,
            height = 54.dp,
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onChangePasswordClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "비밀번호 변경",
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = DayTodoTheme.colors.iconDefault,
            )
        }
    }
}
