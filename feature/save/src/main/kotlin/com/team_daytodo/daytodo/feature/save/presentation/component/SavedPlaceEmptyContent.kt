package com.team_daytodo.daytodo.feature.save.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.uikit.R as UIKitR
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun SavedPlaceEmptyContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = UIKitR.drawable.ic_symbol),
            contentDescription = null,
            tint = Color(0xFFD9D9D9),
            modifier = Modifier.size(width = 42.dp, height = 68.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            style = DayTodoTheme.typography.caption1,
            color = DayTodoTheme.colors.textTertiary,
        )
    }
}
