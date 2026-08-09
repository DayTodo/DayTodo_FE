package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.terms.PolicyListItem
import com.team_daytodo.daytodo.feature.mypage.model.PolicyListTitles
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp

@Composable
fun TermsScreen(
    onBackClick: () -> Unit,
    onDocumentClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "약관 및 정책", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = HorizontalPadding)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PolicyListTitles.forEachIndexed { index, title ->
                PolicyListItem(
                    text = title,
                    onClick = { onDocumentClick(index) },
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun TermsScreenPreview() {
    DayTodoTheme {
        TermsScreen(
            onBackClick = {},
            onDocumentClick = {},
        )
    }
}
