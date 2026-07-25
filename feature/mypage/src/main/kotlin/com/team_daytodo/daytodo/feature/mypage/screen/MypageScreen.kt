package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.R
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageDivider
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageToggleSwitch
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.mypage.MypageMenuGroup
import com.team_daytodo.daytodo.feature.mypage.component.mypage.MypageMenuRow
import com.team_daytodo.daytodo.feature.mypage.component.mypage.ProfileEditButton
import com.team_daytodo.daytodo.feature.mypage.model.MypageProfile
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp

/** 탈퇴하기 전용 색. uikit 팔레트에 대응 토큰이 없어 지시에 따라 하드코딩한다. */
private val WithdrawTextColor = Color(0xFFFF607E)

@Composable
fun MypageScreen(
    onEditProfileClick: () -> Unit = {},
    onManageRegionClick: () -> Unit = {},
    onSendFeedbackClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {},
    notificationEnabled: Boolean = false,
    onNotificationToggle: (Boolean) -> Unit = {},
    // API 연동 전 더미 데이터
    profile: MypageProfile = MypageProfile(nickname = "데이투두"),
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            // 바텀 네비 최상위 화면이라 뒤로가기 버튼 없음(onBackClick 미전달).
            MypageTopBar(title = "마이페이지")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            // (1) 프로필 행
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HorizontalPadding)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(DayTodoTheme.colors.backgroundSecondary),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = profile.nickname,
                    style = DayTodoTheme.typography.label1,
                    color = DayTodoTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                ProfileEditButton(onClick = onEditProfileClick)
            }

            Spacer(modifier = Modifier.height(24.dp))
            MypageDivider()

            Spacer(modifier = Modifier.height(32.dp))
            MypageMenuGroup(
                title = "관리",
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            ) {
                MypageMenuRow(
                    text = "관심지역 설정하기",
                    leadingIconRes = R.drawable.ic_map,
                    trailingArrow = true,
                    onClick = onManageRegionClick,
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
            MypageMenuGroup(
                title = "고객센터",
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            ) {
                MypageMenuRow(
                    text = "의견 보내기",
                    leadingIconRes = R.drawable.ic_chat,
                    trailingArrow = true,
                    onClick = onSendFeedbackClick,
                )
                MypageMenuRow(
                    text = "약관 및 정책",
                    leadingIconRes = R.drawable.ic_article,
                    trailingArrow = true,
                    onClick = onTermsClick,
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HorizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "알림 설정",
                    style = DayTodoTheme.typography.title1,
                    color = DayTodoTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f),
                )
                MypageToggleSwitch(
                    checked = notificationEnabled,
                    onCheckedChange = onNotificationToggle,
                    // 메뉴 행 화살표(MypageMenuRow 의 ArrowEndPadding)와 같은 20dp 로 맞춘다.
                    modifier = Modifier.padding(end = 20.dp),
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
            MypageMenuGroup(
                title = "기타",
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            ) {
                MypageMenuRow(text = "로그아웃", onClick = onLogoutClick)
                MypageMenuRow(
                    text = "탈퇴하기",
                    // uikit 팔레트에 없는 값이라 지시대로 하드코딩. 토큰 추가되면 교체 예정.
                    color = WithdrawTextColor,
                    onClick = onWithdrawClick,
                )
            }

            // 하단 여백(스펙 미지정) — 플로팅 바텀 네비에 마지막 항목이 가려지지 않도록 확보.
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun MypageScreenPreview() {
    DayTodoTheme {
        var notificationEnabled by remember { mutableStateOf(false) }
        MypageScreen(
            notificationEnabled = notificationEnabled,
            onNotificationToggle = { notificationEnabled = it },
        )
    }
}
