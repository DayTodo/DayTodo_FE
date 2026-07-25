package com.team_daytodo.daytodo.feature.mypage.component.profileedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * "개인정보 수정" 그룹: 이름/닉네임/이메일 표시 필드 + 비밀번호 변경 + 전화번호 행.
 * 값은 모두 상위에서 UiState 로 내려받아 그대로 표시만 한다.
 */
@Composable
fun PersonalInfoSection(
    name: String,
    nickname: String,
    email: String,
    phoneNumber: String,
    onChangePasswordClick: () -> Unit,
    onChangePhoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ProfileInfoField(label = "이름", value = name)
        Spacer(modifier = Modifier.height(20.dp))
        ProfileInfoField(label = "닉네임", value = nickname)
        Spacer(modifier = Modifier.height(20.dp))
        // 이메일만 연회색 배경 + 테두리 없는 스타일, 높이도 54dp 로 다르다.
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

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "전화번호",
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = phoneNumber,
                style = DayTodoTheme.typography.label2,
                color = DayTodoTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.weight(1f))
            ProfileGreyButton(text = "변경하기", onClick = onChangePhoneClick)
        }
    }
}
