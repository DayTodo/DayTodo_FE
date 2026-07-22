package com.team_daytodo.daytodo.uikit.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DayTodoFloatingMenuItem(
    val text: String,
    val onClick: () -> Unit,
)

@Composable
fun DayTodoFloatingActionMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    menuItems: List<DayTodoFloatingMenuItem>,
    collapsedFabContent: @Composable BoxScope.() -> Unit,
    expandedFabContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    fabSize: Dp = 56.dp,
    menuSpacing: Dp = 4.dp,
    fabSpacing: Dp = 12.dp,
    expandedFabContainerColor: Color = Color(0xFFC3C7C5),
) {
    val fabInteractionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        menuItems.forEachIndexed { index, menuItem ->
            AnimatedMenuContent(
                visible = expanded,
                delayMillis = (menuItems.lastIndex - index) * 90,
            ) {
                DayTodoPillButton(
                    text = menuItem.text,
                    onClick = {
                        onExpandedChange(false)
                        menuItem.onClick()
                    },
                )
            }

            if (index != menuItems.lastIndex) {
                AnimatedMenuSpacer(
                    visible = expanded,
                    height = menuSpacing,
                    delayMillis = (menuItems.lastIndex - index) * 90,
                )
            }
        }

        AnimatedMenuSpacer(
            visible = expanded,
            height = fabSpacing,
            delayMillis = 0,
        )

        Surface(
            modifier = Modifier
                .size(fabSize)
                .clip(CircleShape)
                .clickable(
                    interactionSource = fabInteractionSource,
                    indication = ripple(
                        bounded = true,
                        radius = fabSize / 2,
                    ),
                    onClick = { onExpandedChange(!expanded) },
                ),
            shape = CircleShape,
            color = if (expanded) expandedFabContainerColor else Color.Transparent,
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (expanded) {
                    expandedFabContent()
                } else {
                    collapsedFabContent()
                }
            }
        }
    }
}

@Composable
private fun AnimatedMenuContent(
    visible: Boolean,
    delayMillis: Int,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = 220,
                delayMillis = delayMillis,
            ),
            initialOffsetY = { it + 18 },
        ) + expandVertically(
            animationSpec = tween(
                durationMillis = 220,
                delayMillis = delayMillis,
            ),
            expandFrom = Alignment.Bottom,
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 140,
                delayMillis = delayMillis,
            ),
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 140),
            targetOffsetY = { it + 18 },
        ) + shrinkVertically(
            animationSpec = tween(durationMillis = 140),
            shrinkTowards = Alignment.Bottom,
        ) + fadeOut(animationSpec = tween(durationMillis = 90)),
    ) {
        content()
    }
}

@Composable
private fun AnimatedMenuSpacer(
    visible: Boolean,
    height: Dp,
    delayMillis: Int,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = 120,
                delayMillis = delayMillis,
            ),
            expandFrom = Alignment.Bottom,
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 90),
            shrinkTowards = Alignment.Bottom,
        ),
    ) {
        Spacer(modifier = Modifier.height(height))
    }
}
