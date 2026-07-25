package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.profileedit.LinkedAccountSection
import com.team_daytodo.daytodo.feature.mypage.component.profileedit.PersonalInfoSection
import com.team_daytodo.daytodo.feature.mypage.component.profileedit.ProfilePhotoPicker
import com.team_daytodo.daytodo.feature.mypage.component.profileedit.ProfileSaveButton
import com.team_daytodo.daytodo.feature.mypage.state.ProfileEditUiState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp

@Composable
fun ProfileEditScreen(
    uiState: ProfileEditUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onChangePhoneClick: () -> Unit = {},
    onUnlinkAccountClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "프로필 관리", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = HorizontalPadding)
                .verticalScroll(rememberScrollState()),
        ) {

            Spacer(modifier = Modifier.height(23.dp))
            ProfilePhotoPicker(
                onClick = onChangePhotoClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "사진을 눌러 프로필 사진을 변경할 수 있어요",
                style = DayTodoTheme.typography.caption2,
                color = DayTodoTheme.colors.textSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )


            Spacer(modifier = Modifier.height(24.dp))
            PersonalInfoSection(
                name = uiState.name,
                nickname = uiState.nickname,
                email = uiState.email,
                phoneNumber = uiState.phoneNumber,
                onChangePasswordClick = onChangePasswordClick,
                onChangePhoneClick = onChangePhoneClick,
            )

            Spacer(modifier = Modifier.height(20.dp))
            LinkedAccountSection(
                providerName = uiState.linkedAccountProvider,
                accountId = uiState.linkedAccountId,
                onUnlinkClick = onUnlinkAccountClick,
            )

            Spacer(modifier = Modifier.height(33.dp))
            ProfileSaveButton(text = "저장하기", onClick = onSaveClick)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun ProfileEditScreenPreview() {
    DayTodoTheme {
        ProfileEditScreen(
            uiState = ProfileEditUiState(
                name = "홍길동",
                nickname = "데이투두",
                email = "daytodo@example.com",
                phoneNumber = "000-0000-0000",
                linkedAccountProvider = "네이버",
                linkedAccountId = "daytodo@naver.com",
            ),
        )
    }
}
