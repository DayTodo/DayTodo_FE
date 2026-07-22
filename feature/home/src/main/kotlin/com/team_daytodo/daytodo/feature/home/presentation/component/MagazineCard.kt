package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.R
import com.team_daytodo.daytodo.feature.home.model.HomeMagazineUiModel
import com.team_daytodo.daytodo.uikit.R as UIKitR
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun MagazineCard(
    magazine: HomeMagazineUiModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .padding(horizontal = 20.dp)
            .drawBehind {
                drawLine(
                    color = Color(0xFFD9D9D9),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Color(0xFFF3F3F3)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = magazine.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(modifier = Modifier.width(26.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 9.5.dp, bottom = 13.5.dp),
        ) {
            Text(
                text = magazine.title,
                style = DayTodoTheme.typography.title2,
                color = DayTodoTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = magazine.location,
                style = DayTodoTheme.typography.caption2,
                color = DayTodoTheme.colors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = magazine.description,
                style = DayTodoTheme.typography.body3,
                color = DayTodoTheme.colors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                modifier = Modifier
                    .width(8.dp)
                    .height(14.dp),
                painter = painterResource(id = UIKitR.drawable.ic_next),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMagazineCard() {
    MagazineCard(
        magazine = HomeMagazineUiModel(
            title = "비 오는 날에 딱 맞는 실내 데이트",
            location = "서울 성수",
            description = "날씨에 흔들리지 않는 전시, 카페, 작은 식당 코스를 골라봤어요.",
            imageRes = R.drawable.ic_symbol,
        ),
        onClick = {},
    )
}
