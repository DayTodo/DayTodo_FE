package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.onboarding.model.OnboardingIllustration
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun OnboardingPairedIllustrationImage(
    illustration: OnboardingIllustration,
    modifier: Modifier = Modifier,
) {
    val secondaryImageRes = illustration.secondaryImageRes

    if (secondaryImageRes == null) {
        OnboardingSingleIllustrationImage(
            illustration = illustration,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(PairedIllustrationAreaAspectRatio),
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(PairedIllustrationAreaAspectRatio),
    ) {
        Image(
            painter = painterResource(id = illustration.primaryImageRes),
            contentDescription = illustration.contentDescription,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(PairedFirstImageWidthFraction)
                .aspectRatio(PairedFirstImageAspectRatio),
            contentScale = ContentScale.FillBounds,
        )

        Image(
            painter = painterResource(id = secondaryImageRes),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxWidth(PairedSecondImageWidthFraction)
                .aspectRatio(PairedSecondImageAspectRatio),
            contentScale = ContentScale.FillBounds,
        )

        PairedIllustrationArrow(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun PairedIllustrationArrow(modifier: Modifier = Modifier) {
    val connectorColor = DayTodoTheme.colors.brandPrimary

    Canvas(modifier = modifier) {
        val startX = size.width * PairedArrowStartXFraction
        val startY = size.height * PairedFirstImageHeightFraction
        val endX = size.width * PairedArrowEndXFraction
        val endY = size.height * PairedArrowEndYFraction
        val cornerRadius = 18.dp.toPx()
        val arrowLength = 8.dp.toPx()
        val arrowWing = 4.dp.toPx()

        val path = Path().apply {
            moveTo(startX, startY)
            lineTo(startX, endY - cornerRadius)
            quadraticTo(startX, endY, startX + cornerRadius, endY)
            lineTo(endX - arrowLength, endY)
        }

        drawPath(
            path = path,
            color = connectorColor,
            style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round),
        )
        drawLine(
            color = connectorColor,
            start = Offset(endX - arrowLength, endY),
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = connectorColor,
            start = Offset(endX - arrowWing, endY - arrowWing),
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = connectorColor,
            start = Offset(endX - arrowWing, endY + arrowWing),
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}

private const val PairedIllustrationAreaAspectRatio = 372f / 342f
private const val PairedFirstImageWidthFraction = 146f / 372f
private const val PairedSecondImageWidthFraction = 210f / 372f
private const val PairedFirstImageAspectRatio = 146f / 87f
private const val PairedSecondImageAspectRatio = 210f / 342f
private const val PairedFirstImageHeightFraction = 87f / 342f
private const val PairedArrowStartXFraction = 43f / 372f
private const val PairedArrowEndXFraction = 146f / 372f
private const val PairedArrowEndYFraction = 219f / 342f
