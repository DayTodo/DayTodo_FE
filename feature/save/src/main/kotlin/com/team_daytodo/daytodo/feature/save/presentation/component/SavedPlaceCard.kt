package com.team_daytodo.daytodo.feature.save.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace
import com.team_daytodo.daytodo.uikit.R as UIKitR
import com.team_daytodo.daytodo.uikit.component.dayTodoPressedScaleClickable
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun SavedPlaceCard(
    place: MagazinePlace,
    selected: Boolean,
    selectionMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = SavedPlaceCardMinHeight)
            .clip(cardShape)
            .dayTodoPressedScaleClickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SavedPlaceImage(
                place = place,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SavedPlaceImageHeight),
            )
            SavedPlaceInfo(
                place = place,
                selected = selected,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = SavedPlaceInfoMinHeight),
            )
        }

        if (selectionMode && selected) {
            SelectionCheckIcon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp),
            )
        }
    }
}

@Composable
private fun SavedPlaceImage(
    place: MagazinePlace,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(place.placeholderBrush()),
        contentAlignment = Alignment.Center,
    ) {
        // place.imageUrl은 매거진 저장(BookmarkItemDto.thumbnailUrl)에서 그대로 내려오는 값인데,
        // 지금까지 이 컴포저블이 항상 그라디언트 placeholder만 그리고 있어 실제 썸네일이 있어도
        // 절대 보이지 않았다.
        if (!place.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = "${place.name} 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                painter = painterResource(id = UIKitR.drawable.ic_logo),
                contentDescription = "${place.name} 이미지",
                tint = Color.White.copy(alpha = 0.72f),
                modifier = Modifier.size(width = 78.dp, height = 23.dp),
            )
        }
    }
}

@Composable
private fun SavedPlaceInfo(
    place: MagazinePlace,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (selected) Color(0xFFA09FF5) else Color(0xFFECECFF)
    val titleColor = if (selected) Color.White else Color(0xFF616166)
    val regionColor = if (selected) Color(0xFFE4E4E4) else Color(0xFF616166)
    val categoryColor = if (selected) Color(0xFFE4E4E4) else Color(0xFF959595)

    Column(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Text(
            text = place.name,
            style = DayTodoTheme.typography.title1,
            color = titleColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = place.region,
            style = DayTodoTheme.typography.caption2,
            color = regionColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = place.category,
            style = DayTodoTheme.typography.caption2,
            color = categoryColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SelectionCheckIcon(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "선택됨",
            tint = Color.White,
            modifier = Modifier.size(18.dp),
        )
    }
}

private fun MagazinePlace.placeholderBrush(): Brush {
    val colors = when {
        "카페" in categoryPath || category.contains("카페") ->
            listOf(Color(0xFFE8F2EF), Color(0xFF88B9A8))
        "전시" in categoryPath ->
            listOf(Color(0xFFEAE8FF), Color(0xFFA09FF5))
        "서점" in categoryPath ->
            listOf(Color(0xFFF5E7D4), Color(0xFFC09C7B))
        "한강" in categoryPath || "야외" in category ->
            listOf(Color(0xFFE6F1FF), Color(0xFF77A7DB))
        "식물원" in categoryPath || category.contains("식물") ->
            listOf(Color(0xFFE8F4DC), Color(0xFF7FAB72))
        else ->
            listOf(Color(0xFFECECFF), Color(0xFFA09FF5))
    }

    return Brush.verticalGradient(colors)
}

private val SavedPlaceImageHeight = 161.dp
private val SavedPlaceInfoMinHeight = 82.dp
private val SavedPlaceCardMinHeight = SavedPlaceImageHeight + SavedPlaceInfoMinHeight
