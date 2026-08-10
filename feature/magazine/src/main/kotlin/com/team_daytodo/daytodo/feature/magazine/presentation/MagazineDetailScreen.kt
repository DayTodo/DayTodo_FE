package com.team_daytodo.daytodo.feature.magazine.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.domain.magazine.model.MagazinePlace
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
            place = uiState.place,
            loading = uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(318.dp),
        )

        val place = uiState.place
        if (place == null) {
            MagazineFallbackBody(
                message = uiState.errorMessage ?: "매거진을 불러오는 중이에요",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            MagazineDetailBody(
                place = place,
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
    place: MagazinePlace?,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(place?.placeholderBrush() ?: LoadingBrush)
            .alpha(if (loading) 0.82f else 1f),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = UIKitR.drawable.ic_symbol),
            contentDescription = place?.let { "${it.name} 이미지" },
            tint = Color.White.copy(alpha = 0.76f),
            modifier = Modifier.size(width = 72.dp, height = 118.dp),
        )
    }
}

@Composable
private fun MagazineDetailBody(
    place: MagazinePlace,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        Text(
            text = place.categoryPathText,
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
                    text = place.name,
                    style = DayTodoTheme.typography.headlineLarge,
                    color = Color(0xFF616166),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = place.address,
                    style = DayTodoTheme.typography.body3,
                    color = Color(0xFF959595),
                )
            }
            DayTodoBookmarkButton(
                saved = place.isSaved,
                onClick = onBookmarkClick,
                modifier = Modifier.padding(top = 0.5.dp),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "가게번호: ${place.phoneNumber}",
            style = DayTodoTheme.typography.body3,
            color = Color(0xFF959595),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = place.description,
            style = DayTodoTheme.typography.body2,
            color = Color(0xFF616166),
        )
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

private val LoadingBrush = Brush.verticalGradient(
    listOf(
        Color(0xFFECECFF),
        Color(0xFFD7D7FF),
    ),
)
