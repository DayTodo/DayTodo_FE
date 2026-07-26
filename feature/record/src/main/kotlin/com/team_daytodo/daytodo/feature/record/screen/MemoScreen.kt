package com.team_daytodo.daytodo.feature.record.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.record.component.MemoInputBar
import com.team_daytodo.daytodo.feature.record.component.MemoItem
import com.team_daytodo.daytodo.feature.record.model.MemoEntry
import com.team_daytodo.daytodo.feature.record.model.RecordPhoto
import com.team_daytodo.daytodo.feature.record.model.sampleRecordUiState
import com.team_daytodo.daytodo.uikit.R
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun MemoScreen(
    photos: List<RecordPhoto>,
    memosByPhotoId: Map<String, List<MemoEntry>>,
    initialPhotoIndex: Int = 0,
    onBackClick: () -> Unit = {},
    onSubmitMemo: (photoId: String, content: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) return

    var currentPhotoIndex by remember(photos) {
        mutableIntStateOf(initialPhotoIndex.coerceIn(0, photos.lastIndex))
    }

    val currentPhoto = photos[currentPhotoIndex]
    val currentMemos = memosByPhotoId[currentPhoto.id].orEmpty()

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
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            MemoPhotoPager(
                photoRes = currentPhoto.imageRes,
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

            // 사진이 바뀌면 입력창의 임시 입력값도 초기화되도록 photo index 로 key 를 건다.
            key(currentPhotoIndex) {
                MemoListSection(
                    memos = currentMemos,
                    onSubmit = { content -> onSubmitMemo(currentPhoto.id, content) },
                )
            }
        }
    }
}

@Composable
private fun MemoListSection(
    memos: List<MemoEntry>,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        memos.forEach { memo ->
            MemoItem(memo = memo)
        }
    }

    MemoInputBar(
        value = inputValue,
        onValueChange = { inputValue = it },
        onSubmit = {
            if (inputValue.isNotBlank()) {
                onSubmit(inputValue)
                inputValue = ""
            }
        },
        focusRequester = focusRequester,
    )
}

@Composable
private fun MemoPhotoPager(
    photoRes: Int?,
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
        if (photoRes != null) {
            Image(
                painter = painterResource(id = photoRes),
                contentDescription = "추억 사진 ${currentIndex + 1} / $photoCount",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

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
            photos = uiState.selectedPhotos,
            memosByPhotoId = uiState.memosByPhotoId,
        )
    }
}
