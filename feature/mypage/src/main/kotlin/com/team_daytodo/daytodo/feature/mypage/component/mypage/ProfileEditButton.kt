package com.team_daytodo.daytodo.feature.mypage.component.mypage

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team_daytodo.daytodo.feature.mypage.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

/**
 * 프로필 수정(연필) 버튼.
 * 업로드된 ic_edit 에셋을 사용한다.
 */
@Composable
fun ProfileEditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_edit),
        contentDescription = "프로필 수정",
        tint = DayTodoTheme.colors.iconDefault,
        modifier = modifier.clickable(onClick = onClick),
    )
}
