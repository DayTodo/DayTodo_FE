package com.team_daytodo.daytodo.feature.course.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.core.model.Relationship
import com.team_daytodo.daytodo.feature.course.R
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldBorderColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.fieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.CourseCreateDefaults.secondaryTextColor
import com.team_daytodo.daytodo.feature.course.presentation.defaults.relationshipColors
import com.team_daytodo.daytodo.feature.course.presentation.defaults.relationshipDescription
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
fun RelationshipCard(
    relationship: Relationship,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = relationshipColors(relationship)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) colors.background else Color.White)
            .border(1.dp, fieldBorderColor, RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = relationship.label,
                style = DayTodoTheme.typography.title1,
                color = fieldContentColor,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = relationshipDescription(relationship),
                style = DayTodoTheme.typography.body3,
                color = secondaryTextColor,
                maxLines = 1,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_relationship),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 21.dp)
                .size(58.dp),
            tint = colors.icon,
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 13.dp, end = 9.dp)
                    .size(RelationshipCheckSize)
                    .clip(CircleShape)
                    .background(colors.icon),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(RelationshipCheckIconSize),
                    tint = Color.White,
                )
            }
        }
    }
}

private val RelationshipCheckSize = 24.dp
private val RelationshipCheckIconSize = 16.dp