package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.domain.mypage.model.InterestRegionOption
import com.team_daytodo.daytodo.feature.mypage.component.common.MypageTopBar
import com.team_daytodo.daytodo.feature.mypage.component.profileedit.ProfileSaveButton
import com.team_daytodo.daytodo.feature.mypage.state.InterestRegionGroup
import com.team_daytodo.daytodo.feature.mypage.state.InterestRegionUiState
import com.team_daytodo.daytodo.uikit.component.DayTodoAuthCheckbox
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp
private val ParentColumnWidth = 84.dp
private val SaveButtonBottomPadding = 20.dp

@Composable
fun InterestRegionScreen(
    uiState: InterestRegionUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onGroupSelected: (String) -> Unit = {},
    onRegionToggled: (Long) -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            MypageTopBar(title = "관심지역 설정", onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = HorizontalPadding),
            ) {
                LazyColumn(
                    modifier = Modifier.width(ParentColumnWidth),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(uiState.groups, key = { it.parentName }) { group ->
                        InterestRegionParentItem(
                            text = group.parentName,
                            selected = group.parentName == uiState.selectedGroupName,
                            onClick = { onGroupSelected(group.parentName) },
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(bottom = 96.dp),
                ) {
                    val children = uiState.selectedGroup?.options.orEmpty()
                    items(children, key = { it.regionId }) { option ->
                        DayTodoAuthCheckbox(
                            checked = option.regionId in uiState.selectedRegionIds,
                            label = option.regionName,
                            onCheckedChange = { onRegionToggled(option.regionId) },
                            modifier = Modifier.padding(vertical = 6.dp),
                        )
                    }
                }
            }

            ProfileSaveButton(
                text = "확인",
                onClick = onSaveClick,
                enabled = uiState.canSave,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = HorizontalPadding)
                    .padding(bottom = SaveButtonBottomPadding),
            )
        }
    }
}

@Composable
private fun InterestRegionParentItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) DayTodoTheme.colors.backgroundSecondary else DayTodoTheme.colors.backgroundDefault,
            )
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label2,
            color = if (selected) DayTodoTheme.colors.brandPrimary else DayTodoTheme.colors.textSecondary,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun InterestRegionScreenPreview() {
    DayTodoTheme {
        InterestRegionScreen(
            uiState = InterestRegionUiState(
                groups = listOf(
                    InterestRegionGroup(
                        parentName = "서울",
                        options = listOf(
                            InterestRegionOption(
                                regionId = 1,
                                regionName = "서울 전체",
                                parentRegionName = "서울",
                            ),
                        ),
                    ),
                ),
                selectedGroupName = "서울",
            ),
        )
    }
}
