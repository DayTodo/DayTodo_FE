package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.sundayColor
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun CourseBudgetRangeFields(
    minBudgetDigits: String,
    onMinBudgetChange: (String) -> Unit,
    maxBudgetDigits: String,
    onMaxBudgetChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BudgetTextField(
                value = minBudgetDigits,
                onValueChange = onMinBudgetChange,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_tilde),
                contentDescription = null,
                tint = fieldContentColor,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(24.dp))
            BudgetTextField(
                value = maxBudgetDigits,
                onValueChange = onMaxBudgetChange,
                modifier = Modifier.weight(1f),
            )
        }
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                style = DayTodoTheme.typography.caption2.copy(letterSpacing = 0.sp),
                color = sundayColor,
            )
        }
    }
}
