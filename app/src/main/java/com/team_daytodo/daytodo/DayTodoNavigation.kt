package com.team_daytodo.daytodo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team_daytodo.daytodo.feature.auth.LoginRoute
import com.team_daytodo.daytodo.feature.auth.ProfileSetupRoute
import com.team_daytodo.daytodo.feature.auth.ResetPasswordRoute
import com.team_daytodo.daytodo.feature.auth.SignupRoute
import com.team_daytodo.daytodo.feature.calendar.CalendarScreen
import com.team_daytodo.daytodo.feature.course.CourseCreateRoute
import com.team_daytodo.daytodo.feature.course.presentation.CourseScreen
import com.team_daytodo.daytodo.feature.course.InviteCodeJoinScreen
import com.team_daytodo.daytodo.feature.record.RecordScreen
import com.team_daytodo.daytodo.feature.home.HomeRoute
import com.team_daytodo.daytodo.feature.mypage.MypageScreen
import com.team_daytodo.daytodo.feature.save.SaveScreen
import com.team_daytodo.daytodo.feature.today.TodayScreen

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
            TodayScreen()
        }
        composable(DayTodoRoute.Record) {
            RecordScreen()
        }
        composable(DayTodoRoute.Mypage) {
            MypageScreen()
        }
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
