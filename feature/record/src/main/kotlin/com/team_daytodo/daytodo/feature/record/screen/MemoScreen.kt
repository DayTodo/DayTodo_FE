package com.team_daytodo.daytodo.feature.record.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
fun MemoScreen(
    photos: List<RecordPhoto>,
    diaryContent: String,
    initialPhotoIndex: Int = 0,
    onBackClick: () -> Unit = {},
    onDiaryContentChange: (String) -> Unit = {},
    onSaveDiaryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) return

    var currentPhotoIndex by remember(photos) {
        mutableIntStateOf(initialPhotoIndex.coerceIn(0, photos.lastIndex))
    }

    val currentPhoto = photos[currentPhotoIndex]

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
                        text = "기록",
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
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            MemoPhotoPager(
                imageUrl = currentPhoto.imageUrl,
                currentIndex = currentPhotoIndex,
                photoCount = photos.size,
                isFirst = currentPhotoIndex == 0,
                isLast = currentPhotoIndex == photos.lastIndex,
                onPrevious = {
                    if (currentPhotoIndex > 0) currentPhotoIndex--
                },
                onNext = {
                    if (currentPhotoIndex < photos.lastIndex) currentPhotoIndex++
                },
            )

            DiaryContentSection(
                content = diaryContent,
                onContentChange = onDiaryContentChange,
                onSaveClick = onSaveDiaryClick,
            )
        }
    }
}

/**
 * 코스/날짜 단위로 공유되는 일기 입력 영역. 어떤 사진을 보고 있든 동일한 내용을 표시한다.
 */
@Composable
private fun DiaryContentSection(
    content: String,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Text(
            text = "오늘의 기록",
            style = DayTodoTheme.typography.label2,
            color = DayTodoTheme.colors.textPrimary,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DayTodoTheme.colors.backgroundSecondary)
                .padding(16.dp),
        ) {
            BasicTextField(
                value = content,
                onValueChange = onContentChange,
                textStyle = DayTodoTheme.typography.label3.copy(
                    color = DayTodoTheme.colors.textPrimary,
                ),
                cursorBrush = SolidColor(DayTodoTheme.colors.brandPrimary),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = "오늘의 기록을 남겨보세요",
                            style = DayTodoTheme.typography.label3,
                            color = DayTodoTheme.colors.textTertiary,
                        )
                    }
                    innerTextField()
                },
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.End)
                .clip(RoundedCornerShape(8.dp))
                .background(DayTodoTheme.colors.brandPrimary)
                .clickable(onClick = onSaveClick)
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text(
                text = "저장",
                style = DayTodoTheme.typography.label3,
                color = DayTodoTheme.colors.iconOnColor,
            )
        }
    }
}

@Composable
private fun MemoPhotoPager(
    imageUrl: String,
    currentIndex: Int,
    photoCount: Int,
    isFirst: Boolean,
    isLast: Boolean,
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
        AsyncImage(
            model = imageUrl,
            contentDescription = "추억 사진 ${currentIndex + 1} / $photoCount",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        PhotoArrowButton(
            icon = Icons.Default.KeyboardArrowLeft,
            contentDescription = "이전 사진",
            enabled = !isFirst,
            onClick = onPrevious,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        PhotoArrowButton(
            icon = Icons.Default.KeyboardArrowRight,
            contentDescription = "다음 사진",
            enabled = !isLast,
            onClick = onNext,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun PhotoArrowButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    enabled: Boolean,
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
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
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
    val uiState = sampleRecordUiState()
    DayTodoTheme {
        MemoScreen(
            photos = uiState.photos,
            diaryContent = uiState.diaryContent,
        )
    }
}
