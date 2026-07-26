package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.domain.onboarding.model.OnboardingVisualType
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun OnboardingFeaturePreview(
    visualType: OnboardingVisualType,
    modifier: Modifier = Modifier,
) {
    when (visualType) {
        OnboardingVisualType.PickMagazine -> MagazinePickPreview(modifier)
        OnboardingVisualType.CourseName -> CourseNamePreview(modifier)
        OnboardingVisualType.InviteLink -> InviteLinkPreview(modifier)
        OnboardingVisualType.AddPlaces -> AddPlacesPreview(modifier)
        OnboardingVisualType.PlanTogether,
        OnboardingVisualType.MemoryRecord,
        OnboardingVisualType.Welcome -> Unit
    }
}

@Composable
private fun MagazinePickPreview(modifier: Modifier = Modifier) {
    PreviewSurface(modifier = modifier.fillMaxWidth().height(176.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "오늘의 Pick! 매거진",
                style = DayTodoTheme.typography.caption1.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = DayTodoTheme.colors.textPrimary,
            )
            MagazinePlaceRow(
                title = "플라워 피크닉",
                description = "푸릇한 정원과 함께 걷는 여유",
                badgeText = "새 코스방 만들기",
                thumbnailBrush = Brush.verticalGradient(
                    listOf(Color(0xFFB6D7C8), Color(0xFF7B8F69)),
                ),
            )
            MagazinePlaceRow(
                title = "그레이트 바이브",
                description = "넓고 포근한 공간에서 편안하게",
                badgeText = "스레드 그룹 참여하기",
                thumbnailBrush = Brush.verticalGradient(
                    listOf(Color(0xFFD7B88E), Color(0xFF7A5E42)),
                ),
            )
        }
    }
}

@Composable
private fun MagazinePlaceRow(
    title: String,
    description: String,
    badgeText: String,
    thumbnailBrush: Brush,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(thumbnailBrush),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = DayTodoTheme.typography.caption2.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = description,
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(DayTodoTheme.colors.brandPrimary.copy(alpha = 0.18f))
                .padding(horizontal = 9.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = badgeText,
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.brandPrimary,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun CourseNamePreview(modifier: Modifier = Modifier) {
    PreviewSurface(modifier = modifier.fillMaxWidth().height(178.dp)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "<",
                    style = DayTodoTheme.typography.caption1,
                    color = DayTodoTheme.colors.textPrimary,
                )
                Text(
                    text = "코스 방 만들기",
                    modifier = Modifier.weight(1f),
                    style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.textPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(DayTodoTheme.colors.divider.copy(alpha = 0.35f)),
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(DayTodoTheme.colors.backgroundSecondary),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(999.dp))
                        .background(DayTodoTheme.colors.brandPrimary),
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
            ) {
                Text(
                    text = "코스 방의\n이름을 지어 주세요",
                    style = DayTodoTheme.typography.title2.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "ex) 벚꽃 나들이 코스",
                        style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                        color = DayTodoTheme.colors.textTertiary,
                    )
                }
            }
        }
    }
}

@Composable
private fun InviteLinkPreview(modifier: Modifier = Modifier) {
    PreviewSurface(modifier = modifier.fillMaxWidth().height(178.dp)) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OnboardingMascotPairIllustration(
                modifier = Modifier
                    .width(96.dp)
                    .height(68.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "그룹이 생성 됐어요",
                style = DayTodoTheme.typography.caption1.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = DayTodoTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(DayTodoTheme.colors.brandPrimary.copy(alpha = 0.12f))
                    .padding(horizontal = 14.dp, vertical = 7.dp),
            ) {
                Text(
                    text = "링크가 복사되었어요",
                    style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.brandPrimary,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "daytodo.link/flower-picnic",
                    style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun AddPlacesPreview(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(220.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        CourseTicketPreview()
        ArrowConnector(
            modifier = Modifier
                .width(34.dp)
                .height(52.dp),
        )
        PhoneCoursePreview()
    }
}

@Composable
private fun CourseTicketPreview() {
    PreviewSurface(
        modifier = Modifier
            .width(92.dp)
            .height(68.dp),
        radius = 10.dp,
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "성수동 밤",
                style = DayTodoTheme.typography.caption2.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.sp,
                ),
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
            )
            Text(
                text = "올리브 식탁",
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textTertiary,
                maxLines = 1,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFFFE75D).copy(alpha = 0.35f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = "D-2",
                    style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.brandPrimary,
                )
            }
        }
    }
}

@Composable
private fun ArrowConnector(modifier: Modifier = Modifier) {
    val arrowColor = DayTodoTheme.colors.brandPrimary.copy(alpha = 0.6f)

    Canvas(modifier = modifier) {
        val centerY = size.height / 2f
        drawLine(
            color = arrowColor,
            start = Offset(0f, centerY),
            end = Offset(size.width * 0.82f, centerY),
            strokeWidth = 2.dp.toPx(),
        )
        drawLine(
            color = arrowColor,
            start = Offset(size.width * 0.82f, centerY),
            end = Offset(size.width * 0.66f, centerY - size.height * 0.14f),
            strokeWidth = 2.dp.toPx(),
        )
        drawLine(
            color = arrowColor,
            start = Offset(size.width * 0.82f, centerY),
            end = Offset(size.width * 0.66f, centerY + size.height * 0.14f),
            strokeWidth = 2.dp.toPx(),
        )
    }
}

@Composable
private fun PhoneCoursePreview() {
    PreviewSurface(
        modifier = Modifier
            .width(128.dp)
            .height(198.dp),
        radius = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "<",
                    style = DayTodoTheme.typography.caption2,
                    color = DayTodoTheme.colors.textPrimary,
                )
                Text(
                    text = "장소 추천",
                    modifier = Modifier.weight(1f),
                    style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                    color = DayTodoTheme.colors.textPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    maxLines = 1,
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(DayTodoTheme.colors.brandPrimary.copy(alpha = 0.22f)),
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFCFA271), Color(0xFF77583D)),
                        ),
                    ),
            )
            PhonePlaceRow(title = "한강 밤마실", selected = true)
            PhonePlaceRow(title = "브런치 카페", selected = false)
        }
    }
}

@Composable
private fun PhonePlaceRow(
    title: String,
    selected: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (selected) {
                        DayTodoTheme.colors.brandPrimary.copy(alpha = 0.18f)
                    } else {
                        DayTodoTheme.colors.backgroundSecondary
                    },
                ),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(DayTodoTheme.colors.divider.copy(alpha = 0.42f)),
            )
        }
    }
}

@Composable
private fun PreviewSurface(
    modifier: Modifier = Modifier,
    radius: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(radius)
    Surface(
        modifier = modifier,
        shape = shape,
        color = DayTodoTheme.colors.backgroundTertiary,
        border = BorderStroke(
            width = 1.dp,
            color = DayTodoTheme.colors.brandPrimary.copy(alpha = 0.55f),
        ),
        content = content,
    )
}
