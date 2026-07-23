package com.team_daytodo.daytodo.feature.record.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.component.MemoEntry
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class MemoPhoto(
    val id: String,
    val memos: List<MemoEntry>,
)

// 더미 데이터: 사진 3장. 첫 사진에만 댓글이 있고 나머지는 빈 상태.
private val dummyMemoPhotos = listOf(
    MemoPhoto(
        id = "photo-1",
        memos = listOf(
            MemoEntry(id = "m1", author = "나", content = "소품샵 어때 😊"),
            MemoEntry(id = "m2", author = "보라", content = "다음에 곰볼 갔다가 헤이티도 고고"),
            MemoEntry(id = "m3", author = "나", content = "고고고"),
        ),
    ),
    MemoPhoto(id = "photo-2", memos = emptyList()),
    MemoPhoto(id = "photo-3", memos = emptyList()),
)

@Composable
fun MemoScreen(
    onBackClick: () -> Unit = {},
    photos: List<MemoPhoto> = dummyMemoPhotos,
    modifier: Modifier = Modifier,
) {
    var currentPhotoIndex by remember { mutableIntStateOf(0) }

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
                        text = "메모",
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
                .padding(innerPadding),
        ) {
            MemoPhotoPager(
                photoCount = photos.size,
                currentIndex = currentPhotoIndex,
                onPrevious = {
                    if (currentPhotoIndex > 0) currentPhotoIndex--
                },
                onNext = {
                    if (currentPhotoIndex < photos.lastIndex) currentPhotoIndex++
                },
            )

            Text(
                text = "메모 남기기",
                style = DayTodoTheme.typography.label2,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 16.dp,
                ),
            )

            // 댓글 작성 기능 연결 전 자리 표시용 박스 (UI만)
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DayTodoTheme.colors.backgroundSecondary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "작성하기",
                    style = DayTodoTheme.typography.label3,
                    color = DayTodoTheme.colors.textTertiary,
                )
            }
        }
    }
}

/**
 * 추억 사진 영역. 사진 위에 좌/우 화살표를 플로팅으로 겹쳐 배치한다.
 * 실제 이미지 로딩은 다음 단계이며, 지금은 placeholder 정사각형이다.
 */
@Composable
private fun MemoPhotoPager(
    photoCount: Int,
    currentIndex: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(DayTodoTheme.colors.backgroundSecondary),
        contentAlignment = Alignment.Center,
    ) {
        // placeholder: 현재 사진 위치 표시
        Text(
            text = "${currentIndex + 1} / $photoCount",
            style = DayTodoTheme.typography.caption1,
            color = DayTodoTheme.colors.textTertiary,
        )

        PhotoArrowButton(
            icon = Icons.Default.KeyboardArrowLeft,
            contentDescription = "이전 사진",
            onClick = onPrevious,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        PhotoArrowButton(
            icon = Icons.Default.KeyboardArrowRight,
            contentDescription = "다음 사진",
            onClick = onNext,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun PhotoArrowButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(20.dp)
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            // TODO: DayTodoTheme.colors 에 연보라 배경 토큰 추가 후 교체 (임시 하드코딩)
            .background(Color(0xFFE0E0F5))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = DayTodoTheme.colors.brandPrimary,
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun MemoScreenPreview() {
    DayTodoTheme {
        MemoScreen()
    }
}
