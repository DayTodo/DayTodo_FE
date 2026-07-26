package com.team_daytodo.daytodo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team_daytodo.daytodo.feature.auth.FindPasswordRoute
import com.team_daytodo.daytodo.feature.auth.LoginRoute
import com.team_daytodo.daytodo.feature.auth.ProfileSetupRoute
import com.team_daytodo.daytodo.feature.auth.ResetPasswordRoute
import com.team_daytodo.daytodo.feature.auth.SignupRoute
import com.team_daytodo.daytodo.feature.calendar.CalendarScreen
import com.team_daytodo.daytodo.feature.course.CourseCreateRoute
import com.team_daytodo.daytodo.feature.course.presentation.CourseScreen
import com.team_daytodo.daytodo.feature.course.InviteCodeJoinScreen
import com.team_daytodo.daytodo.feature.record.navigation.RecordNavHost
import com.team_daytodo.daytodo.feature.home.HomeRoute
import com.team_daytodo.daytodo.feature.mypage.navigation.mypageNavGraph
import com.team_daytodo.daytodo.feature.save.SaveScreen
import com.team_daytodo.daytodo.feature.today.screen.TodayRoute

@Composable
internal fun DayTodoNavHost(
    navController: NavHostController,
    onTodayScheduleChanged: (Boolean) -> Unit,
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = DayTodoRoute.Login,
    ) {
        composable(DayTodoRoute.Login) {
            LoginRoute(
                onNavigateToSignup = { navController.navigateSingleTopTo(DayTodoRoute.Signup) },
                onNavigateToFindPassword = {
                    navController.navigateSingleTopTo(DayTodoRoute.FindPassword)
                },
                onLoginCompleted = { needsProfileSetup ->
                    if (needsProfileSetup) {
                        navController.navigateSingleTopTo(DayTodoRoute.ProfileSetup)
                    } else {
                        navController.navigateToHomeClearingAuth()
                    }
                },
            )
        }
        composable(DayTodoRoute.Signup) {
            SignupRoute(
                onBackClick = { navController.popBackStack() },
                onSignupCompleted = { needsProfileSetup ->
                    if (needsProfileSetup) {
                        navController.navigateSingleTopTo(DayTodoRoute.ProfileSetup)
                    } else {
                        navController.navigateToHomeClearingAuth()
                    }
                },
            )
        }
        composable(DayTodoRoute.FindPassword) {
            FindPasswordRoute(
                onBackClick = { navController.popBackStack() },
                onVerificationCompleted = { verificationToken ->
                    navController.navigateSingleTopTo(
                        DayTodoRoute.resetPasswordRoute(verificationToken),
                    )
                },
            )
        }
        composable(
            route = DayTodoRoute.ResetPassword,
            arguments = listOf(
                navArgument(DayTodoRoute.ResetPasswordTokenArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            ResetPasswordRoute(
                verificationToken = backStackEntry.arguments
                    ?.getString(DayTodoRoute.ResetPasswordTokenArg)
                    .orEmpty(),
                onPasswordResetCompleted = {
                    navController.popBackStack(
                        route = DayTodoRoute.Login,
                        inclusive = false,
                    )
                },
            )
        }
        composable(DayTodoRoute.ProfileSetup) {
            ProfileSetupRoute(
                onBackClick = { navController.navigateToHomeClearingAuth() },
                onProfileSaved = { navController.navigateToHomeClearingAuth() },
            )
        }
        composable(DayTodoRoute.Home) {
            HomeRoute(
                onNavigateToSave = { navController.navigateSingleTopTo(DayTodoRoute.Save) },
                onNavigateToCalendar = { navController.navigateSingleTopTo(DayTodoRoute.Calendar) },
                onNavigateToCourseList = { navController.navigateSingleTopTo(DayTodoRoute.Course) },
                onNavigateToCourseCreate = { navController.navigateSingleTopTo(DayTodoRoute.CourseCreate) },
                onNavigateToCourseJoin = { navController.navigateSingleTopTo(DayTodoRoute.CourseJoin) },
                onTodayScheduleChanged = onTodayScheduleChanged,
            )
        }
        composable(DayTodoRoute.Save) {
            SaveScreen()
        }
        composable(DayTodoRoute.Calendar) {
            CalendarScreen()
        }
        composable(DayTodoRoute.Course) {
            CourseScreen()
        }
        composable(DayTodoRoute.CourseCreate) {
            CourseCreateRoute(
                onBackClick = { navController.popBackStack() },
                onDoneClick = {
                    val didPopToHome = navController.popBackStack(
                        route = DayTodoRoute.Home,
                        inclusive = false,
                    )
                    if (!didPopToHome) {
                        navController.navigateSingleTopTo(DayTodoRoute.Home)
                    }
                },
            )
        }
        composable(DayTodoRoute.CourseJoin) {
            InviteCodeJoinScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(DayTodoRoute.Today) {
            TodayRoute()
        }
        composable(DayTodoRoute.Record) {
            RecordNavHost()
        }
        // 마이페이지("mypage") + 프로필 관리 라우트를 함께 등록한다.
        // MypageRoute.Mypage 값이 DayTodoRoute.Mypage 와 같아 바텀 네비와 그대로 호환된다.
        mypageNavGraph(navController)
    }
}

private fun NavHostController.navigateToHomeClearingAuth() {
    navigate(DayTodoRoute.Home) {
        popUpTo(DayTodoRoute.Login) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

internal fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) {
        launchSingleTop = true
    }
}

internal fun NavHostController.navigateToTopLevelDestination(route: String) {
    if (route == DayTodoRoute.Home) {
        val didPopToHome = popBackStack(
            route = DayTodoRoute.Home,
            inclusive = false,
        )
        if (!didPopToHome) {
            navigateSingleTopTo(DayTodoRoute.Home)
        }
        return
    }

    navigate(route) {
        popUpTo(DayTodoRoute.Home) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
