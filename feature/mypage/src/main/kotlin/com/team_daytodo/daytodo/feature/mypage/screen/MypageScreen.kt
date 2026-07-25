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
import com.team_daytodo.daytodo.feature.mypage.state.MypageDialogState
import com.team_daytodo.daytodo.uikit.dialog.DayTodoAlertDialog
import com.team_daytodo.daytodo.uikit.dialog.DayTodoMessageDialog
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp

private val WithdrawTextColor = Color(0xFFFF607E)

private const val LogoutConfirmMessage = "다시 이용하시려면\n재로그인이 필요합니다."
private const val LogoutDoneMessage = "안전하게 로그아웃되었습니다.\n다음에 다시 만나요!"
private const val WithdrawConfirmMessage =
    "탈퇴 시 모든 계정 정보와 이용 내역이\n영구적으로 삭제되며\n복구할 수 없습니다."
private const val WithdrawDoneMessage = "안전하게 탈퇴되었습니다.\n다음에 다시 만나요!"

@Composable
fun MypageScreen(
    onEditProfileClick: () -> Unit = {},
    onManageRegionClick: () -> Unit = {},
    onSendFeedbackClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    notificationEnabled: Boolean = false,
    onNotificationToggle: (Boolean) -> Unit = {},
    profile: MypageProfile = MypageProfile(nickname = "데이투두"),
    modifier: Modifier = Modifier,
) {
    var dialogState by remember { mutableStateOf<MypageDialogState>(MypageDialogState.None) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "마이페이지")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
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
                    modifier = Modifier.padding(end = 20.dp),
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
            MypageMenuGroup(
                title = "기타",
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            ) {
                MypageMenuRow(
                    text = "로그아웃",
                    onClick = { dialogState = MypageDialogState.LogoutConfirm },
                )
                MypageMenuRow(
                    text = "탈퇴하기",
                    color = WithdrawTextColor,
                    onClick = { dialogState = MypageDialogState.WithdrawConfirm },
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    MypageDialogHost(
        state = dialogState,
        onStateChange = { dialogState = it },
        onNavigateToLogin = onNavigateToLogin,
    )
}

@Composable
private fun MypageDialogHost(
    state: MypageDialogState,
    onStateChange: (MypageDialogState) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val dismiss = { onStateChange(MypageDialogState.None) }
    val finish = {
        onStateChange(MypageDialogState.None)
        onNavigateToLogin()
    }

    when (state) {
        MypageDialogState.None -> Unit

        MypageDialogState.LogoutConfirm -> DayTodoAlertDialog(
            title = "주의",
            message = LogoutConfirmMessage,
            onConfirm = { onStateChange(MypageDialogState.LogoutDone) },
            onDismiss = dismiss,
        )

        MypageDialogState.LogoutDone -> DayTodoMessageDialog(
            message = LogoutDoneMessage,
            onDismiss = finish,
        )

        MypageDialogState.WithdrawConfirm -> DayTodoAlertDialog(
            title = "주의",
            message = WithdrawConfirmMessage,
            onConfirm = { onStateChange(MypageDialogState.WithdrawDone) },
            onDismiss = dismiss,
        )

        MypageDialogState.WithdrawDone -> DayTodoMessageDialog(
            message = WithdrawDoneMessage,
            onDismiss = finish,
        )
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
