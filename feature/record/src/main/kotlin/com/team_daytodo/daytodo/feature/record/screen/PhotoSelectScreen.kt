package com.team_daytodo.daytodo.feature.record.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team_daytodo.daytodo.domain.record.model.RecordPhoto
import com.team_daytodo.daytodo.feature.record.model.sampleRecordUiState
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun PhotoSelectScreen(
    photos: List<RecordPhoto>,
    onBackClick: () -> Unit = {},
    onPhotoClick: (Int) -> Unit = {},
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
                text = "사진을 선택해 기록을 남겨보세요",
                style = DayTodoTheme.typography.label2,
                color = DayTodoTheme.colors.textPrimary,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(
                    items = photos,
                    key = { _, photo -> photo.memoryPhotoId },
                ) { index, photo ->
                    PhotoGridCell(
                        photo = photo,
                        onClick = { onPhotoClick(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoGridCell(
    photo: RecordPhoto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // RecordPhotoRow.PhotoThumbnail와 동일한 이유(업로드 API 부재로 로컬 갤러리 Uri가 그대로
    // imageUrl로 쓰이는 경우가 있어, 세션이 바뀌면 권한 만료로 영영 로딩되지 않을 수 있음)로
    // 로딩 실패 상태를 별도 표시한다.
    var loadFailed by remember(photo.imageUrl) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(DayTodoTheme.colors.backgroundSecondary)
            .clickable(onClick = onClick),
    ) {
        if (loadFailed) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "사진을 표시할 수 없어요",
                tint = DayTodoTheme.colors.iconDefault,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onError = { loadFailed = true },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun PhotoSelectScreenPreview() {
    DayTodoTheme {
        PhotoSelectScreen(photos = sampleRecordUiState().photos)
    }
}
