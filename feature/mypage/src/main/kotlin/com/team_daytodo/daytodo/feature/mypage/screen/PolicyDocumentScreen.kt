package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.terms.PolicyBodyText
import com.team_daytodo.daytodo.feature.mypage.state.PolicyDocumentUiState
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val ContentHorizontalPadding = 20.dp

@Composable
fun PolicyDocumentScreen(
    title: String,
    uiState: PolicyDocumentUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = title, onBackClick = onBackClick)
        },
    ) { innerPadding ->
        when (uiState) {
            is PolicyDocumentUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = DayTodoTheme.colors.brandPrimary)
                }
            }

            is PolicyDocumentUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "문서를 불러오지 못했어요",
                        style = DayTodoTheme.typography.body3,
                        color = DayTodoTheme.colors.textSecondary,
                    )
                }
            }

            is PolicyDocumentUiState.Content -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = ContentHorizontalPadding)
                        .padding(top = 20.dp, bottom = 20.dp)
                        .navigationBarsPadding(),
                ) {
                    PolicyBodyText(body = uiState.body)
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun PolicyDocumentScreenPreview() {
    DayTodoTheme {
        PolicyDocumentScreen(
            title = "만 14세 이상 확인",
            uiState = PolicyDocumentUiState.Content("미리보기 본문"),
            onBackClick = {},
        )
    }
}
