package com.team_daytodo.daytodo.feature.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

private val HorizontalPadding = 20.dp
private val ParentColumnWidth = 84.dp
private val SaveButtonBottomPadding = 100.dp
private val ParentItemWidth = 72.dp
private val ParentItemHeight = 46.dp

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
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(44.dp))
                Text(
                    text = "관심 지역을 설정해주세요.",
                    style = DayTodoTheme.typography.headlineLarge,
                    color = DayTodoTheme.colors.textPrimary,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "관심 지역을 선택하고 나에게 맞는 코스와 장소를 추천받아보세요.",
                    style = DayTodoTheme.typography.body3,
                    color = DayTodoTheme.colors.textSecondary,
                    modifier = Modifier.padding(horizontal = HorizontalPadding),
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                            InterestRegionChildItem(
                                text = option.regionName,
                                selected = option.regionId in uiState.selectedRegionIds,
                                onClick = { onRegionToggled(option.regionId) },
                            )
                        }
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
            .width(ParentItemWidth)
            .height(ParentItemHeight)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) DayTodoTheme.colors.backgroundSecondary else DayTodoTheme.colors.backgroundDefault,
            )
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.caption1,
            color = if (selected) DayTodoTheme.colors.brandPrimary else DayTodoTheme.colors.textSecondary,
            maxLines = 1,
        )
    }
}

@Composable
private fun InterestRegionChildItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 13.dp),
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label2,
            color = if (selected) DayTodoTheme.colors.brandPrimary else DayTodoTheme.colors.textSecondary,
        )
    }
}

// Preview 전용 더미 데이터 — 실제 화면 로직/API 호출과는 무관하며 GetRegionsUseCase를 대체하지 않는다.
@Preview(showBackground = true, heightDp = 900)
@Composable
private fun InterestRegionScreenAllRegionsPreview() {
    val groups = sampleAllInterestRegionGroups()
    DayTodoTheme {
        InterestRegionScreen(
            uiState = InterestRegionUiState(
                groups = groups,
                selectedGroupName = groups.first { it.parentName == "서울" }.parentName,
                selectedRegionIds = setOf(2L),
            ),
        )
    }
}

private fun sampleAllInterestRegionGroups(): List<InterestRegionGroup> {
    var nextId = 1L

    fun standalone(name: String): InterestRegionGroup {
        val id = nextId++
        return InterestRegionGroup(
            parentName = name,
            options = listOf(InterestRegionOption(id, name, null)),
        )
    }

    fun group(parentName: String, children: List<String>): InterestRegionGroup =
        InterestRegionGroup(
            parentName = parentName,
            options = children.map { childName -> InterestRegionOption(nextId++, childName, parentName) },
        )

    return listOf(
        standalone("전국"),
        group(
            "서울",
            listOf(
                "서울 전체",
                "홍대/합정/망원/연남",
                "강남/역삼/서초",
                "성수/건대/왕십리",
                "종로/을지로/동대문",
                "여의도/영등포",
                "잠실/송파/강동",
                "용산/이태원/한남",
            ),
        ),
        group(
            "부산",
            listOf(
                "부산 전체",
                "해운대/센텀/송정",
                "광안리/수영/민락",
                "서면/전포/부전",
                "남포/자갈치/영도",
                "부산역/초량/동구",
                "동래/온천장/명륜",
                "기장/일광/정관",
            ),
        ),
        group(
            "제주",
            listOf(
                "제주 전체",
                "제주시/제주공항",
                "애월/한림/협재",
                "조천/함덕/김녕",
                "성산/우도",
                "서귀포/중문",
                "표선/남원",
            ),
        ),
        group(
            "인천",
            listOf(
                "인천 전체",
                "송도/연수",
                "구월/인천터미널",
                "부평/부개",
                "청라/서구",
                "영종/인천공항",
                "강화/석모도",
            ),
        ),
        group(
            "강원",
            listOf(
                "강원 전체",
                "춘천",
                "원주",
                "강릉",
                "속초/고성",
                "양양/동해",
                "평창/대관령",
            ),
        ),
        group(
            "경기",
            listOf(
                "경기 전체",
                "수원/광교",
                "성남/분당/판교",
                "용인/수지/기흥",
                "고양/일산",
                "김포/파주",
                "부천/광명",
                "안양/과천",
                "하남/구리",
                "의정부/양주/동두천",
            ),
        ),
        group(
            "경상",
            listOf(
                "경상 전체",
                "대구",
                "울산",
                "경주",
                "포항",
                "안동",
                "창원/마산/진해",
                "진주/사천",
                "거제/통영",
            ),
        ),
        group(
            "전라",
            listOf(
                "전라 전체",
                "광주",
                "전주",
                "군산/익산",
                "여수",
                "순천/광양",
                "목포/무안",
                "담양/곡성/구례",
                "해남/완도/진도",
            ),
        ),
    )
}
