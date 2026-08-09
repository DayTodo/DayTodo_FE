package com.team_daytodo.daytodo

import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.oauth.util.NidOAuthCallback
import com.team_daytodo.daytodo.feature.auth.FindPasswordRoute
import com.team_daytodo.daytodo.feature.auth.LoginRoute
import com.team_daytodo.daytodo.feature.auth.ProfileSetupRoute
import com.team_daytodo.daytodo.feature.auth.ResetPasswordRoute
import com.team_daytodo.daytodo.feature.auth.SignupRoute
import com.team_daytodo.daytodo.feature.calendar.CalendarScreen
import com.team_daytodo.daytodo.feature.course.CourseCreateRoute
import com.team_daytodo.daytodo.feature.course.CourseEditRoute
import com.team_daytodo.daytodo.feature.course.CourseListRoute
import com.team_daytodo.daytodo.feature.course.InviteCodeJoinScreen
import com.team_daytodo.daytodo.feature.course.PlaceCommentRoute
import com.team_daytodo.daytodo.feature.course.PlaceRecommendationRoute
import com.team_daytodo.daytodo.feature.home.HomeRoute
import com.team_daytodo.daytodo.feature.magazine.MagazineRoute
import com.team_daytodo.daytodo.feature.mypage.navigation.mypageNavGraph
import com.team_daytodo.daytodo.feature.onboarding.OnboardingGateRoute
import com.team_daytodo.daytodo.feature.onboarding.OnboardingRoute
import com.team_daytodo.daytodo.feature.record.navigation.recordNavGraph
import com.team_daytodo.daytodo.feature.save.SaveRoute
import com.team_daytodo.daytodo.feature.save.SavedPlacePickerRoute
import com.team_daytodo.daytodo.feature.today.screen.TodayRoute

@Composable
internal fun DayTodoNavHost(
    navController: NavHostController,
    onTodayScheduleChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = DayTodoRoute.OnboardingGate,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(DayTodoRoute.OnboardingGate) {
            OnboardingGateRoute(
                onShowOnboarding = {
                    navController.navigateClearingOnboardingGate(DayTodoRoute.Onboarding)
                },
                onSkipOnboarding = {
                    navController.navigateClearingOnboardingGate(DayTodoRoute.Login)
                },
            )
        }
        composable(DayTodoRoute.Onboarding) {
            OnboardingRoute(
                onOnboardingCompleted = {
                    navController.navigateToLoginClearingOnboarding()
                },
            )
        }
        composable(DayTodoRoute.Login) {
            val context = LocalContext.current

            LoginRoute(
                onNavigateToSignup = { navController.navigateSingleTopTo(DayTodoRoute.Signup) },
                onNavigateToFindPassword = {
                    navController.navigateSingleTopTo(DayTodoRoute.FindPassword)
                },
                onNaverLoginClick = { onAccessTokenReceived, onFailure ->
                    authenticateWithNaver(
                        context = context,
                        onAccessTokenReceived = onAccessTokenReceived,
                        onFailure = onFailure,
                    )
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
                onSignupCompleted = {
                    navController.popBackStack(
                        route = DayTodoRoute.Login,
                        inclusive = false,
                    )
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
                onMagazineClick = { placeId ->
                    navController.navigateSingleTopTo(DayTodoRoute.magazineDetailRoute(placeId))
                },
                onTodayScheduleChanged = onTodayScheduleChanged,
            )
        }
        composable(DayTodoRoute.Save) {
            SaveRoute(
                onBackClick = { navController.popBackStack() },
                onPlaceClick = { placeId ->
                    navController.navigateSingleTopTo(DayTodoRoute.magazineDetailRoute(placeId))
                },
            )
        }
        composable(
            route = DayTodoRoute.MagazineDetail,
            arguments = listOf(
                navArgument(DayTodoRoute.PlaceIdArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            MagazineRoute(
                placeId = backStackEntry.arguments
                    ?.getString(DayTodoRoute.PlaceIdArg)
                    .orEmpty(),
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(DayTodoRoute.Today) {
            TodayRoute(
                onAddPlaceClick = { courseId ->
                    navController.navigate(DayTodoRoute.placeRecommendationRoute(courseId.toString()))
                },
            )
        }
        composable(DayTodoRoute.Calendar) {
            CalendarScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(DayTodoRoute.Course) {
            CourseListRoute(
                onBackClick = { navController.popBackStack() },
                onCourseClick = { courseId ->
                    navController.navigateSingleTopTo(
                        DayTodoRoute.placeRecommendationRoute(courseId),
                    )
                },
            )
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
        composable(
            route = DayTodoRoute.PlaceRecommendation,
            arguments = listOf(
                navArgument(DayTodoRoute.CourseIdArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments
                ?.getString(DayTodoRoute.CourseIdArg)
                .orEmpty()
            val savedPlacesImported by backStackEntry.savedStateHandle
                .getStateFlow(DayTodoRoute.SavedPlacesImportedArg, false)
                .collectAsStateWithLifecycle()
            PlaceRecommendationRoute(
                courseId = courseId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { selectedCourseId ->
                    navController.navigateSingleTopTo(
                        DayTodoRoute.courseEditRoute(selectedCourseId),
                    )
                },
                onCommentClick = { selectedCourseId, placeId ->
                    navController.navigateSingleTopTo(
                        DayTodoRoute.placeCommentRoute(
                            courseId = selectedCourseId,
                            placeId = placeId,
                        ),
                    )
                },
                importedSavedPlaces = savedPlacesImported,
                onImportedSavedPlacesHandled = {
                    backStackEntry.savedStateHandle[DayTodoRoute.SavedPlacesImportedArg] = false
                },
                onSavedPlaceClick = {
                    navController.navigateSingleTopTo(
                        DayTodoRoute.savedPlacePickerRoute(courseId),
                    )
                },
            )
        }
        composable(
            route = DayTodoRoute.SavedPlacePicker,
            arguments = listOf(
                navArgument(DayTodoRoute.CourseIdArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            SavedPlacePickerRoute(
                courseId = backStackEntry.arguments
                    ?.getString(DayTodoRoute.CourseIdArg)
                    .orEmpty(),
                onBackClick = { navController.popBackStack() },
                onImportCompleted = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(DayTodoRoute.SavedPlacesImportedArg, true)
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = DayTodoRoute.CourseEdit,
            arguments = listOf(
                navArgument(DayTodoRoute.CourseIdArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            CourseEditRoute(
                courseId = backStackEntry.arguments
                    ?.getString(DayTodoRoute.CourseIdArg)
                    .orEmpty(),
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(
            route = DayTodoRoute.PlaceComment,
            arguments = listOf(
                navArgument(DayTodoRoute.CourseIdArg) {
                    type = NavType.StringType
                },
                navArgument(DayTodoRoute.PlaceIdArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            PlaceCommentRoute(
                courseId = backStackEntry.arguments
                    ?.getString(DayTodoRoute.CourseIdArg)
                    .orEmpty(),
                placeId = backStackEntry.arguments
                    ?.getString(DayTodoRoute.PlaceIdArg)
                    .orEmpty(),
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(DayTodoRoute.Today) {
            TodayRoute()
        }
        recordNavGraph(navController)
        mypageNavGraph(navController)
    }
}

private fun authenticateWithNaver(
    context: Context,
    onAccessTokenReceived: (String) -> Unit,
    onFailure: (String) -> Unit,
) {
    val clientId = BuildConfig.NAVER_CLIENT_ID
    val clientSecret = BuildConfig.NAVER_CLIENT_SECRET
    val clientName = BuildConfig.NAVER_CLIENT_NAME

    if (clientId.isBlank() || clientSecret.isBlank() || clientName.isBlank()) {
        onFailure("\ub124\uc774\ubc84 \ub85c\uadf8\uc778 \uc124\uc815\uac12\uc744 \ud655\uc778\ud574 \uc8fc\uc138\uc694.")
        return
    }

    NidOAuth.initialize(
        context.applicationContext,
        clientId,
        clientSecret,
        clientName,
    )

    NidOAuth.requestLogin(
        context,
        object : NidOAuthCallback {
            override fun onSuccess() {
                val accessToken = NidOAuth.getAccessToken()
                if (accessToken.isNullOrBlank()) {
                    onFailure("\ub124\uc774\ubc84 \ub85c\uadf8\uc778 \uc815\ubcf4\ub97c \uac00\uc838\uc624\uc9c0 \ubabb\ud588\uc5b4\uc694.")
                    return
                }

                onAccessTokenReceived(accessToken)
            }

            override fun onFailure(errorCode: String, errorDesc: String) {
                val description = NidOAuth.getLastErrorDescription().orEmpty()
                onFailure(
                    description.takeIf(String::isNotBlank)
                        ?: errorDesc.takeIf(String::isNotBlank)
                        ?: "\ub124\uc774\ubc84 \ub85c\uadf8\uc778\uc5d0 \uc2e4\ud328\ud588\uc5b4\uc694.",
                )
            }
        },
    )
}

private fun NavHostController.navigateToHomeClearingAuth() {
    navigate(DayTodoRoute.Home) {
        popUpTo(DayTodoRoute.Login) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateToLoginClearingOnboarding() {
    navigate(DayTodoRoute.Login) {
        popUpTo(DayTodoRoute.Onboarding) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateClearingOnboardingGate(route: String) {
    navigate(route) {
        popUpTo(DayTodoRoute.OnboardingGate) {
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
