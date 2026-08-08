package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.R
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.feedback.FeedbackTextField
import com.team_daytodo.daytodo.feature.mypage.state.FeedbackUiState
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButton
import com.team_daytodo.daytodo.uikit.component.DayTodoNextStepButtonState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun FeedbackScreen(
    uiState: FeedbackUiState,
    onBackClick: () -> Unit,
    onContentChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "의견 보내기", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (uiState.isSubmitted) {
                FeedbackSubmittedContent(onBackClick = onBackClick)
            } else {
                FeedbackInputContent(
                    uiState = uiState,
                    onContentChange = onContentChange,
                    onSubmitClick = onSubmitClick,
                )
            }
        }
    }
}

@Composable
private fun FeedbackInputContent(
    uiState: FeedbackUiState,
    onContentChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(top = 44.dp, bottom = 132.dp),
        ) {
            Text(
                text = "데이투두에 전하고 싶은\n의견이 있나요?",
                style = DayTodoTheme.typography.headlineLarge,
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "데이투두에 전하고 싶은 의견이 있나요?\n남겨주신 의견은 다음 업데이트에 반영돼요",
                style = DayTodoTheme.typography.body3,
                color = DayTodoTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            FeedbackTextField(
                value = uiState.content,
                onValueChange = onContentChange,
                placeholder = "100자 이상 입력해주세요",
            )
        }

        DayTodoNextStepButton(
            text = "전달하기",
            state = when {
                uiState.isSubmitting -> DayTodoNextStepButtonState.Loading
                uiState.canSubmit -> DayTodoNextStepButtonState.Enabled
                else -> DayTodoNextStepButtonState.Disabled
            },
            onClick = onSubmitClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

@Composable
private fun FeedbackSubmittedContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_feedback_duru),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "의견이 전달됐어요",
                style = DayTodoTheme.typography.title1,
                color = DayTodoTheme.colors.textPrimary,
            )
        }

        DayTodoNextStepButton(
            text = "돌아가기",
            state = DayTodoNextStepButtonState.Enabled,
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = ScreenHorizontalPadding)
                .padding(bottom = BottomButtonPadding),
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun FeedbackScreenPreview() {
    DayTodoTheme {
        FeedbackScreen(
            uiState = FeedbackUiState(),
            onBackClick = {},
            onContentChange = {},
            onSubmitClick = {},
        )
    }
}
