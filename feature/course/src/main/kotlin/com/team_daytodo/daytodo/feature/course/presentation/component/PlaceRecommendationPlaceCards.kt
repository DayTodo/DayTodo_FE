package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.course.model.CoursePlace
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceRecommendation
import com.team_daytodo.daytodo.domain.course.model.CoursePlaceSearchResult
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.model.formatWon
import com.team_daytodo.daytodo.uikit.component.DayTodoIconBadgeButton
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun RecommendationPlaceCard(
    index: Int,
    recommendation: CoursePlaceRecommendation,
    currentMemberId: String,
    isInCourse: Boolean,
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onToggleCoursePlaceClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PlaceImageHeader(
                place = recommendation.place,
                index = index,
                modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
            )
            PlaceInfoBlock(
                place = recommendation.place,
                modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DayTodoIconBadgeButton(
                    icon = painterResource(id = R.drawable.ic_like),
                    contentDescription = "좋아요",
                    selected = recommendation.isLikedBy(currentMemberId),
                    badgeCount = recommendation.likedByMemberIds.size,
                    onClick = onLikeClick,
                    width = 56.dp,
                )
                DayTodoIconBadgeButton(
                    icon = painterResource(id = R.drawable.ic_comment),
                    contentDescription = "댓글",
                    badgeCount = 0,
                    onClick = onCommentClick,
                    width = 72.dp,
                )
                CourseContainedButton(
                    contained = isInCourse,
                    onClick = onToggleCoursePlaceClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
internal fun SearchPlaceCard(
    index: Int,
    result: CoursePlaceSearchResult,
    onClick: () -> Unit,
    onRecommendClick: () -> Unit,
    onToggleCoursePlaceClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PlaceImageHeader(
                place = result.place,
                index = index,
                modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
            )
            PlaceInfoBlock(
                place = result.place,
                modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CourseTextActionButton(
                    text = if (result.recommendedByCurrentMember) "추천했어요" else "추천하기",
                    selected = result.recommendedByCurrentMember,
                    enabled = !result.recommendedByCurrentMember,
                    onClick = onRecommendClick,
                    modifier = Modifier.weight(1f),
                )
                CourseTextActionButton(
                    text = if (result.isInCourse) "이미 코스에 담겨 있어요" else "코스에 넣기",
                    selected = !result.isInCourse,
                    onClick = onToggleCoursePlaceClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun PlaceImageHeader(
    place: CoursePlace,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(103.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(place.imageBrush()),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 12.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(DayTodoTheme.colors.brandPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString(),
                style = DayTodoTheme.typography.caption1.copy(letterSpacing = 0.sp),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun PlaceInfoBlock(
    place: CoursePlace,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(114.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = place.name,
                modifier = Modifier.weight(1f),
                style = DayTodoTheme.typography.title1.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "약 ${place.expectedPrice.formatWon()}원",
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.brandPrimary,
                maxLines = 1,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "| ${place.address}, ${place.category} |",
            style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = place.description,
            style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
            color = DayTodoTheme.colors.textSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CourseContainedButton(
    contained: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CourseTextActionButton(
        text = if (contained) "이미 코스에 담겨 있어요" else "코스에 넣기",
        selected = contained,
        onClick = onClick,
        modifier = modifier,
        selectedMeansFilled = true,
    )
}

@Composable
private fun CourseTextActionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedMeansFilled: Boolean = true,
) {
    val filled = if (selectedMeansFilled) selected else !selected
    Box(
        modifier = modifier
            .height(51.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (filled) DayTodoTheme.colors.brandPrimary else Color.White)
            .border(1.dp, DayTodoTheme.colors.brandPrimary, RoundedCornerShape(12.dp))
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DayTodoTheme.typography.label2.copy(
                fontSize = if (text.length > 9) 12.sp else 16.sp,
                letterSpacing = 0.sp,
            ),
            color = if (filled) Color.White else DayTodoTheme.colors.brandPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

private fun CoursePlace.imageBrush(): Brush {
    val colors = when (category) {
        "카페" -> listOf(Color(0xFFE7F3EE), Color(0xFFAFCFC1))
        "공원", "야외" -> listOf(Color(0xFFE7F4D7), Color(0xFF92BD8D))
        "전시" -> listOf(Color(0xFFE9E5FA), Color(0xFFB5B5F5))
        "한식" -> listOf(Color(0xFFFFE7D4), Color(0xFFE8A978))
        "서점" -> listOf(Color(0xFFF6EAD7), Color(0xFFC7A98C))
        else -> listOf(Color(0xFFE7E7FF), Color(0xFFB5B5F5))
    }
    return Brush.verticalGradient(colors)
}
