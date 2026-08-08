package com.team_daytodo.daytodo.feature.mypage.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.team_daytodo.daytodo.feature.mypage.model.MypageProfile
import com.team_daytodo.daytodo.feature.mypage.screen.MypageScreen
import com.team_daytodo.daytodo.feature.mypage.screen.PasswordChangeRoute
import com.team_daytodo.daytodo.feature.mypage.screen.PhoneChangeRoute
import com.team_daytodo.daytodo.feature.mypage.screen.ProfileEditRoute
import com.team_daytodo.daytodo.feature.mypage.viewmodel.MypageViewModel

object MypageRoute {
    const val Mypage = "mypage"
    const val ProfileEdit = "mypage/profile-edit"
    const val PhoneChange = "mypage/phone-change"
    const val PasswordChange = "mypage/password-change"
}

fun NavGraphBuilder.mypageNavGraph(navController: NavController) {
    composable(MypageRoute.Mypage) {
        val viewModel: MypageViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        MypageScreen(
            onEditProfileClick = { navController.navigate(MypageRoute.ProfileEdit) },
            onNavigateToLogin = {},
            notificationEnabled = uiState.notificationEnabled,
            onNotificationToggle = viewModel::toggleNotification,
            dialogState = uiState.dialogState,
            onDialogStateChange = viewModel::onDialogStateChange,
            onWithdrawConfirmClick = viewModel::confirmWithdraw,
            profile = MypageProfile(nickname = uiState.nickname),
        )
    }
    composable(MypageRoute.ProfileEdit) {
        ProfileEditRoute(
            onBackClick = { navController.popBackStack() },
            onChangePasswordClick = { navController.navigate(MypageRoute.PasswordChange) },
            onChangePhoneClick = { navController.navigate(MypageRoute.PhoneChange) },
        )
    }
    composable(MypageRoute.PhoneChange) {
        PhoneChangeRoute(
            onBackClick = { navController.popBackStack() },
        )
    }
    composable(MypageRoute.PasswordChange) {
        PasswordChangeRoute(
            onBackClick = { navController.popBackStack() },
            onPasswordChangeCompleted = {
                navController.popBackStack(
                    route = MypageRoute.ProfileEdit,
                    inclusive = false,
                )
            },
        )
    }
}
