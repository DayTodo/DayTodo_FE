package com.team_daytodo.daytodo.feature.home.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team_daytodo.daytodo.feature.home.R
import com.team_daytodo.daytodo.feature.home.model.CourseMember
import com.team_daytodo.daytodo.uikit.theme.DayTodoTheme

@Composable
internal fun CourseMemberProfile(member: CourseMember) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            member.profileImage?.let { profileImage ->
                Icon(
                    modifier = Modifier
                        .width(12.dp)
                        .height(18.dp),
                    painter = painterResource(id = profileImage),
                    contentDescription = null,
                    tint = DayTodoTheme.colors.iconOnColor,
                )
            }
        }

        Text(
            text = member.name,
            style = DayTodoTheme.typography.title3,
            color = DayTodoTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun PreviewCourseMemberProfile() {
    CourseMemberProfile(
        member = CourseMember(
            name = "민지",
            profileImage = R.drawable.ic_symbol
        )
    )
}