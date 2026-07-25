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

/** 뒤로가기 아이콘(IconButton 기본 크기)이 없을 때도 상단바 높이가 유지되도록 하는 최소 높이. */
private val TopBarMinHeight = 48.dp

/**
 * 투데이 상단바와 동일 구조: (선택) 좌측 뒤로가기 아이콘 + 중앙 타이틀 + 하단 구분선.
 * 마이페이지 / 프로필 관리 화면이 공통으로 사용한다.
 *
 * onBackClick 이 null 이면 뒤로가기 버튼을 그리지 않는다.
 * 마이페이지는 바텀 네비 최상위 화면이라 되돌아갈 곳이 없어 null 로 사용한다.
 */
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
