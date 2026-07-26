package com.team_daytodo.daytodo.feature.mypage.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.team_daytodo.daytodo.feature.mypage.screen.MypageScreen
import com.team_daytodo.daytodo.feature.mypage.screen.ProfileEditRoute

object MypageRoute {
    const val Mypage = "mypage"
    const val ProfileEdit = "mypage/profile-edit"
}

fun NavGraphBuilder.mypageNavGraph(navController: NavController) {
    composable(MypageRoute.Mypage) {
        var notificationEnabled by rememberSaveable { mutableStateOf(false) }

        MypageScreen(
            onEditProfileClick = { navController.navigate(MypageRoute.ProfileEdit) },
            onNavigateToLogin = {},
            notificationEnabled = notificationEnabled,
            onNotificationToggle = { notificationEnabled = it },
        )
    }
    composable(MypageRoute.ProfileEdit) {
        ProfileEditRoute(
            onBackClick = { navController.popBackStack() },
        )
    }
}
