package com.team_daytodo.daytodo.feature.mypage.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team_daytodo.daytodo.feature.mypage.model.MypageProfile
import com.team_daytodo.daytodo.feature.mypage.screen.FeedbackRoute
import com.team_daytodo.daytodo.feature.mypage.screen.InterestRegionRoute
import com.team_daytodo.daytodo.feature.mypage.screen.MypageScreen
import com.team_daytodo.daytodo.feature.mypage.screen.PasswordChangeRoute
import com.team_daytodo.daytodo.feature.mypage.screen.PolicyDocumentRoute
import com.team_daytodo.daytodo.feature.mypage.screen.ProfileEditRoute
import com.team_daytodo.daytodo.feature.mypage.screen.TermsScreen
import com.team_daytodo.daytodo.feature.mypage.viewmodel.MypageViewModel

object MypageRoute {
    const val Mypage = "mypage"
    const val ProfileEdit = "mypage/profile-edit"
    const val InterestRegion = "mypage/interest-region"
    const val PasswordChange = "mypage/password-change"
    const val Terms = "mypage/terms"
    const val PolicyDocumentIndexArg = "documentIndex"
    const val PolicyDocument = "mypage/terms/document/{$PolicyDocumentIndexArg}"
    const val Feedback = "mypage/feedback"

    const val Login = "auth/login"

    fun policyDocumentRoute(documentIndex: Int) = "mypage/terms/document/$documentIndex"
}

fun NavGraphBuilder.mypageNavGraph(navController: NavController) {
    composable(MypageRoute.Mypage) {
        val viewModel: MypageViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        MypageScreen(
            onEditProfileClick = { navController.navigate(MypageRoute.ProfileEdit) },
            onManageRegionClick = { navController.navigate(MypageRoute.InterestRegion) },
            onSendFeedbackClick = { navController.navigate(MypageRoute.Feedback) },
            onTermsClick = { navController.navigate(MypageRoute.Terms) },
            onNavigateToLogin = {
                navController.navigate(MypageRoute.Login) {
                    popUpTo(0) { inclusive = true }
                }
            },
            notificationEnabled = uiState.notificationEnabled,
            onNotificationToggle = viewModel::toggleNotification,
            dialogState = uiState.dialogState,
            onDialogStateChange = viewModel::onDialogStateChange,
            onLogoutConfirmClick = viewModel::confirmLogout,
            onWithdrawConfirmClick = viewModel::confirmWithdraw,
            profile = MypageProfile(nickname = uiState.nickname),
        )
    }
    composable(MypageRoute.ProfileEdit) {
        ProfileEditRoute(
            onBackClick = { navController.popBackStack() },
            onChangePasswordClick = { navController.navigate(MypageRoute.PasswordChange) },
            onProfileSaved = { navController.popBackStack() },
        )
    }
    composable(MypageRoute.InterestRegion) {
        InterestRegionRoute(
            onBackClick = { navController.popBackStack() },
            onSaved = { navController.popBackStack() },
        )
    }
    composable(MypageRoute.PasswordChange) {
        PasswordChangeRoute(
            onBackClick = { navController.popBackStack() },
            onPasswordChangeCompleted = {
                navController.navigate(MypageRoute.Login) {
                    popUpTo(0) { inclusive = true }
                }
            },
        )
    }
    composable(MypageRoute.Terms) {
        TermsScreen(
            onBackClick = { navController.popBackStack() },
            onDocumentClick = { index ->
                navController.navigate(MypageRoute.policyDocumentRoute(documentIndex = index))
            },
        )
    }
    composable(
        route = MypageRoute.PolicyDocument,
        arguments = listOf(
            navArgument(MypageRoute.PolicyDocumentIndexArg) {
                type = NavType.IntType
            },
        ),
    ) { backStackEntry ->
        val documentIndex = backStackEntry.arguments?.getInt(MypageRoute.PolicyDocumentIndexArg) ?: 0
        PolicyDocumentRoute(
            documentIndex = documentIndex,
            onBackClick = { navController.popBackStack() },
        )
    }
    composable(MypageRoute.Feedback) {
        FeedbackRoute(
            onBackClick = { navController.popBackStack() },
        )
    }
}
