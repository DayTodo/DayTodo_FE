package com.team_daytodo.daytodo.feature.magazine.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.team_daytodo.daytodo.domain.magazine.model.MagazineDetail
import com.team_daytodo.daytodo.domain.magazine.model.MagazinePhoto
import com.team_daytodo.daytodo.feature.magazine.model.MagazineUiState
import com.team_daytodo.daytodo.uikit.R as UIKitR
import com.team_daytodo.daytodo.uikit.component.DayTodoBookmarkButton
import com.team_daytodo.daytodo.uikit.component.DayTodoHeaderSection
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun MagazineDetailScreen(
    uiState: MagazineUiState,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        DayTodoHeaderSection(
            title = "매거진",
            onBackClick = onBackClick,
        )

        MagazineHeroImage(
            detail = uiState.detail,
            loading = uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(318.dp),
        )

        val detail = uiState.detail
        if (detail == null) {
            MagazineFallbackBody(
                message = uiState.errorMessage ?: "매거진을 불러오는 중이에요",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            MagazineDetailBody(
                detail = detail,
                isSaved = uiState.isSaved,
                onBookmarkClick = onBookmarkClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun MagazineHeroImage(
    detail: MagazineDetail?,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(LoadingBrush)
            .alpha(if (loading) 0.82f else 1f),
        contentAlignment = Alignment.Center,
    ) {
        if (detail?.thumbnailUrl != null) {
            AsyncImage(
                model = detail.thumbnailUrl,
                contentDescription = "${detail.placeName} 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Icon(
                painter = painterResource(id = UIKitR.drawable.ic_symbol),
                contentDescription = detail?.let { "${it.placeName} 이미지" },
                tint = Color.White.copy(alpha = 0.76f),
                modifier = Modifier.size(width = 72.dp, height = 118.dp),
            )
        }
    }
}

@Composable
private fun MagazineDetailBody(
    detail: MagazineDetail,
    isSaved: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedPhotoIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        Text(
            text = detail.category,
            style = DayTodoTheme.typography.caption2,
            color = Color(0xFF8B8AF5),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = detail.placeName,
                    style = DayTodoTheme.typography.headlineLarge,
                    color = Color(0xFF616166),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = detail.address,
                    style = DayTodoTheme.typography.body3,
                    color = Color(0xFF959595),
                )
            }
            DayTodoBookmarkButton(
                saved = isSaved,
                onClick = onBookmarkClick,
                modifier = Modifier.padding(top = 0.5.dp),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (!detail.businessHours.isNullOrBlank()) {
            Text(
                text = "영업시간: ${detail.businessHours}",
                style = DayTodoTheme.typography.body3,
                color = Color(0xFF959595),
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        if (!detail.phone.isNullOrBlank()) {
            Text(
                text = "가게번호: ${detail.phone}",
                style = DayTodoTheme.typography.body3,
                color = Color(0xFF959595),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        val content = detail.content
        if (!content.isNullOrBlank()) {
            Text(
                text = content,
                style = DayTodoTheme.typography.body2,
                color = Color(0xFF616166),
            )
        }
        if (detail.photos.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            MagazinePhotoRow(
                photos = detail.photos,
                onPhotoClick = { index -> selectedPhotoIndex = index },
            )
        }
    }

    val photoIndex = selectedPhotoIndex
    if (photoIndex != null) {
        MagazinePhotoViewerDialog(
            photos = detail.photos,
            initialIndex = photoIndex,
            onDismiss = { selectedPhotoIndex = null },
        )
    }
}

@Composable
private fun MagazinePhotoRow(
    photos: List<MagazinePhoto>,
    onPhotoClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(photos) { index, photo ->
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(84.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF3F3F3))
                    .clickable { onPhotoClick(index) },
            )
        }
    }
}

// 하단 사진 목록 탭 시 뜨는 스와이프 가능한 전체화면 뷰어.
@Composable
private fun MagazinePhotoViewerDialog(
    photos: List<MagazinePhoto>,
    initialIndex: Int,
    onDismiss: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex.coerceIn(0, photos.lastIndex),
        pageCount = { photos.size },
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                AsyncImage(
                    model = photos[page].imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.White,
                )
            }

            Text(
                text = "${pagerState.currentPage + 1} / ${photos.size}",
                style = DayTodoTheme.typography.caption1,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
            )
        }
    }
}

@Composable
private fun MagazineFallbackBody(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = DayTodoTheme.typography.caption1,
            color = DayTodoTheme.colors.textTertiary,
        )
    }
}

private val LoadingBrush = Brush.verticalGradient(
    listOf(
        Color(0xFFECECFF),
        Color(0xFFD7D7FF),
    ),
)
