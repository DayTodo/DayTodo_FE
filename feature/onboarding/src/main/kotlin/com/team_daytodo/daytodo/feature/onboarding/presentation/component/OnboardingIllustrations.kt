package com.team_daytodo.daytodo.feature.onboarding.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.min

@Composable
internal fun OnboardingMascotPairIllustration(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.aspectRatio(1.42f)) {
        val characterWidth = size.width * 0.34f
        val characterHeight = size.height * 0.58f
        val characterTop = size.height * 0.32f
        val left = Offset(size.width * 0.14f, characterTop)
        val right = Offset(size.width * 0.52f, characterTop)

        drawLine(
            color = MascotColor,
            start = Offset(size.width * 0.50f, size.height * 0.05f),
            end = Offset(size.width * 0.50f, size.height * 0.20f),
            strokeWidth = size.minDimension * 0.02f,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = MascotColor,
            start = Offset(size.width * 0.39f, size.height * 0.09f),
            end = Offset(size.width * 0.44f, size.height * 0.21f),
            strokeWidth = size.minDimension * 0.018f,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = MascotColor,
            start = Offset(size.width * 0.61f, size.height * 0.09f),
            end = Offset(size.width * 0.56f, size.height * 0.21f),
            strokeWidth = size.minDimension * 0.018f,
            cap = StrokeCap.Round,
        )

        drawDayTodoCharacter(
            topLeft = left,
            bodySize = Size(characterWidth, characterHeight),
            facingRight = true,
        )
        drawDayTodoCharacter(
            topLeft = right,
            bodySize = Size(characterWidth, characterHeight),
            facingRight = false,
        )

        val handStroke = size.minDimension * 0.026f
        drawLine(
            color = Color.White,
            start = Offset(size.width * 0.44f, size.height * 0.58f),
            end = Offset(size.width * 0.56f, size.height * 0.58f),
            strokeWidth = handStroke,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
internal fun OnboardingSingleMascotIllustration(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.aspectRatio(0.86f)) {
        drawDayTodoCharacter(
            topLeft = Offset(size.width * 0.18f, size.height * 0.18f),
            bodySize = Size(size.width * 0.64f, size.height * 0.70f),
            facingRight = true,
        )

        val stroke = size.minDimension * 0.025f
        val bubbleRadius = size.minDimension * 0.055f
        drawCircle(
            color = MascotColor.copy(alpha = 0.28f),
            radius = bubbleRadius,
            center = Offset(size.width * 0.72f, size.height * 0.18f),
        )
        drawCircle(
            color = MascotColor.copy(alpha = 0.18f),
            radius = bubbleRadius * 0.7f,
            center = Offset(size.width * 0.82f, size.height * 0.27f),
        )
        drawLine(
            color = Color.White,
            start = Offset(size.width * 0.58f, size.height * 0.47f),
            end = Offset(size.width * 0.82f, size.height * 0.31f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
internal fun OnboardingWelcomePinIllustration(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.aspectRatio(0.72f)) {
        val pinPath = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.04f)
            cubicTo(
                size.width * 0.18f,
                size.height * 0.04f,
                size.width * 0.06f,
                size.height * 0.32f,
                size.width * 0.18f,
                size.height * 0.55f,
            )
            cubicTo(
                size.width * 0.27f,
                size.height * 0.72f,
                size.width * 0.40f,
                size.height * 0.84f,
                size.width * 0.50f,
                size.height * 0.96f,
            )
            cubicTo(
                size.width * 0.60f,
                size.height * 0.84f,
                size.width * 0.73f,
                size.height * 0.72f,
                size.width * 0.82f,
                size.height * 0.55f,
            )
            cubicTo(
                size.width * 0.94f,
                size.height * 0.32f,
                size.width * 0.82f,
                size.height * 0.04f,
                size.width * 0.50f,
                size.height * 0.04f,
            )
            close()
        }

        drawPath(path = pinPath, color = MascotColor)
        drawCircle(
            color = Color.White,
            radius = size.minDimension * 0.055f,
            center = Offset(size.width * 0.50f, size.height * 0.20f),
        )

        drawPinFace(
            start = Offset(size.width * 0.28f, size.height * 0.38f),
            end = Offset(size.width * 0.48f, size.height * 0.60f),
            facingRight = true,
        )
        drawPinFace(
            start = Offset(size.width * 0.72f, size.height * 0.38f),
            end = Offset(size.width * 0.52f, size.height * 0.60f),
            facingRight = false,
        )
    }
}

private fun DrawScope.drawDayTodoCharacter(
    topLeft: Offset,
    bodySize: Size,
    facingRight: Boolean,
) {
    val width = bodySize.width
    val height = bodySize.height
    val bodyHeight = height * 0.82f
    val corner = min(width, height) * 0.24f
    val shadowHeight = height * 0.12f

    drawOval(
        color = ShadowColor,
        topLeft = Offset(topLeft.x + width * 0.06f, topLeft.y + height * 0.86f),
        size = Size(width * 0.88f, shadowHeight),
    )
    drawRoundRect(
        color = MascotColor,
        topLeft = topLeft,
        size = Size(width, bodyHeight),
        cornerRadius = CornerRadius(corner, corner),
    )

    val legWidth = width * 0.17f
    val legHeight = height * 0.24f
    drawRoundRect(
        color = MascotColor,
        topLeft = Offset(topLeft.x + width * 0.18f, topLeft.y + height * 0.70f),
        size = Size(legWidth, legHeight),
        cornerRadius = CornerRadius(legWidth, legWidth),
    )
    drawRoundRect(
        color = MascotColor,
        topLeft = Offset(topLeft.x + width * 0.64f, topLeft.y + height * 0.70f),
        size = Size(legWidth, legHeight),
        cornerRadius = CornerRadius(legWidth, legWidth),
    )

    drawCircle(
        color = Color.White,
        radius = width * 0.055f,
        center = Offset(
            x = if (facingRight) topLeft.x + width * 0.66f else topLeft.x + width * 0.34f,
            y = topLeft.y + height * 0.31f,
        ),
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.80f),
        radius = width * 0.035f,
        center = Offset(
            x = if (facingRight) topLeft.x + width * 0.82f else topLeft.x + width * 0.18f,
            y = topLeft.y + height * 0.43f,
        ),
    )

    val mouthPath = Path().apply {
        if (facingRight) {
            moveTo(topLeft.x + width * 0.60f, topLeft.y + height * 0.48f)
            cubicTo(
                topLeft.x + width * 0.75f,
                topLeft.y + height * 0.64f,
                topLeft.x + width * 0.88f,
                topLeft.y + height * 0.54f,
                topLeft.x + width * 0.88f,
                topLeft.y + height * 0.43f,
            )
        } else {
            moveTo(topLeft.x + width * 0.40f, topLeft.y + height * 0.48f)
            cubicTo(
                topLeft.x + width * 0.25f,
                topLeft.y + height * 0.64f,
                topLeft.x + width * 0.12f,
                topLeft.y + height * 0.54f,
                topLeft.x + width * 0.12f,
                topLeft.y + height * 0.43f,
            )
        }
    }
    drawPath(
        path = mouthPath,
        color = Color.White.copy(alpha = 0.90f),
        style = Stroke(
            width = width * 0.045f,
            cap = StrokeCap.Round,
        ),
    )
}

private fun DrawScope.drawPinFace(
    start: Offset,
    end: Offset,
    facingRight: Boolean,
) {
    val eyeX = if (facingRight) start.x + size.width * 0.06f else start.x - size.width * 0.06f
    drawCircle(
        color = Color.White,
        radius = size.minDimension * 0.026f,
        center = Offset(eyeX, start.y + size.height * 0.03f),
    )

    val facePath = Path().apply {
        moveTo(start.x, start.y)
        cubicTo(
            if (facingRight) start.x + size.width * 0.09f else start.x - size.width * 0.09f,
            start.y + size.height * 0.08f,
            if (facingRight) end.x - size.width * 0.05f else end.x + size.width * 0.05f,
            end.y - size.height * 0.04f,
            end.x,
            end.y,
        )
    }
    drawPath(
        path = facePath,
        color = Color.White,
        style = Stroke(
            width = size.minDimension * 0.030f,
            cap = StrokeCap.Round,
        ),
    )
}

private val MascotColor = Color(0xFF8B8AF5)
private val ShadowColor = Color(0xFFE5E5F4)
