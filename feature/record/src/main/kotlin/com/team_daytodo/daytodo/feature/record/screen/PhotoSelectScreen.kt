package com.team_daytodo.daytodo.feature.record.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

// 갤러리 연동 전 자리 표시용 개수. 실제 사진 데이터 연결은 이후 단계.
private const val PlaceholderPhotoCount = 9

/**
 * "사진 더보기"에서 진입하는 사진 선택 화면.
 * 사진 데이터 연결은 다음 단계이며, 지금은 3열 그리드 플레이스홀더만 표시한다.
 */
@Composable
fun PhotoSelectScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DayTodoTheme.colors.backgroundDefault,
        topBar = {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "뒤로가기",
                        )
                    }
                    Text(
                        text = "사진",
                        style = DayTodoTheme.typography.title1,
                        color = DayTodoTheme.colors.textPrimary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = "사진을 선택해 메모를 남겨보세요",
                style = DayTodoTheme.typography.label2,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(count = PlaceholderPhotoCount) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DayTodoTheme.colors.backgroundSecondary),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun PhotoSelectScreenPreview() {
    DayTodoTheme {
        PhotoSelectScreen()
    }
}
