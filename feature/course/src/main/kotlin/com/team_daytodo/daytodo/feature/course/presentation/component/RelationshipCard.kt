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
import com.team_daytodo.daytodo.feature.course.presentation.FieldBorderColor
import com.team_daytodo.daytodo.feature.course.presentation.FieldContentColor
import com.team_daytodo.daytodo.feature.course.presentation.SecondaryTextColor
import com.team_daytodo.daytodo.feature.course.presentation.relationshipColors
import com.team_daytodo.daytodo.feature.course.presentation.relationshipDescription
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
            .border(1.dp, FieldBorderColor, RoundedCornerShape(12.dp))
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
                color = FieldContentColor,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = relationshipDescription(relationship),
                style = DayTodoTheme.typography.body3,
                color = SecondaryTextColor,
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
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 13.dp, end = 9.dp)
                    .size(24.dp),
                tint = colors.icon,
            )
        }
    }
}