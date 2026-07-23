package com.team_daytodo.daytodo.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

data class DiaryPhoto(
    val id: String,
)

/**
 * 선택된 날짜의 추억 사진 미리보기.
 *
 * - 사진이 있으면 최대 3장 + 같은 행에 "사진 더보기" 텍스트
 * - 사진이 없으면 회색 네모(+ 흰색 플러스) 빈 상태 영역
 */
@Composable
fun DiaryPhotoRow(
    photos: List<DiaryPhoto>,
    onPhotoClick: (DiaryPhoto) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) {
        EmptyPhotoState(modifier = modifier)
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            photos.take(3).forEach { photo ->
                PhotoThumbnail(
                    photo = photo,
                    onClick = onPhotoClick,
                )
            }
            Text(
                text = "사진 더보기",
                style = DayTodoTheme.typography.caption2,
                color = DayTodoTheme.colors.textSecondary,
                modifier = Modifier.clickable(onClick = onMoreClick),
            )
        }
    }
}

@Composable
private fun PhotoThumbnail(
    photo: DiaryPhoto,
    onClick: (DiaryPhoto) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 실제 이미지 로딩은 다음 단계. 지금은 플레이스홀더 정사각형.
    Box(
        modifier = modifier
            .size(84.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = DayTodoTheme.colors.backgroundSecondary)
            .clickable { onClick(photo) },
    )
}

@Composable
private fun EmptyPhotoState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(84.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = DayTodoTheme.colors.backgroundSecondary),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
        )
    }
}
