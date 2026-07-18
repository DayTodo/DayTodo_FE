package com.team_daytodo.daytodo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun FloatingBottomNavigation(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    onDestinationClick: (BottomNavigationDestination) -> Unit,
) {
    val navigationShape = RoundedCornerShape(percent = 50)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(
                elevation = 18.dp,
                shape = navigationShape,
                clip = false,
            ),
        color = DayTodoTheme.colors.backgroundDefault,
        shape = navigationShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            bottomNavigationDestinations.forEach { destination ->
                FloatingBottomNavigationItem(
                    destination = destination,
                    selected = currentRoute == destination.route,
                    onClick = { onDestinationClick(destination) },
                )
            }
        }
    }
}

@Composable
private fun FloatingBottomNavigationItem(
    destination: BottomNavigationDestination,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (selected) {
        DayTodoTheme.colors.iconSelected
    } else {
        DayTodoTheme.colors.iconDisabled
    }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .width(58.dp)
            .height(72.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(top = if (selected) 11.dp else 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = destination.icon.drawableResId),
            contentDescription = null,
            tint = tint,
        )
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = destination.label,
                style = DayTodoTheme.typography.caption2,
                color = tint,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(DayTodoTheme.colors.iconSelected),
            )
        }
    }
}

private val bottomNavigationDestinations = listOf(
    BottomNavigationDestination(
        route = DayTodoRoute.Home,
        label = "홈",
        icon = BottomNavigationIconType.Home,
    ),
    BottomNavigationDestination(
        route = DayTodoRoute.Today,
        label = "투데이",
        icon = BottomNavigationIconType.Today,
    ),
    BottomNavigationDestination(
        route = DayTodoRoute.Record,
        label = "기록",
        icon = BottomNavigationIconType.Record,
    ),
    BottomNavigationDestination(
        route = DayTodoRoute.Mypage,
        label = "마이페이지",
        icon = BottomNavigationIconType.Mypage,
    ),
)

internal data class BottomNavigationDestination(
    val route: String,
    val label: String,
    val icon: BottomNavigationIconType,
)

internal enum class BottomNavigationIconType(
    val drawableResId: Int,
) {
    Home(drawableResId = R.drawable.ic_home),
    Today(drawableResId = R.drawable.ic_today),
    Record(drawableResId = R.drawable.ic_record),
    Mypage(drawableResId = R.drawable.ic_mypage),
}
