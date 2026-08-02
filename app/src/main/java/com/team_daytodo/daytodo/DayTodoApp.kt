package com.team_daytodo.daytodo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun DayTodoApp() {
    val navController = rememberNavController()
    var hasTodaySchedule by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routesWithoutBottomNavigation = DayTodoRoute.AuthRoutes + setOf(
        DayTodoRoute.OnboardingGate,
        DayTodoRoute.Onboarding,
        DayTodoRoute.CourseCreate,
        DayTodoRoute.CourseJoin,
        DayTodoRoute.PlaceRecommendation,
        DayTodoRoute.SavedPlacePicker,
        DayTodoRoute.CourseEdit,
        DayTodoRoute.PlaceComment,
        DayTodoRoute.Save,
        DayTodoRoute.MagazineDetail,
    )
    val showBottomNavigation = currentRoute != null &&
        currentRoute !in routesWithoutBottomNavigation
    val navHostModifier = if (
        currentRoute == null ||
        currentRoute == DayTodoRoute.OnboardingGate ||
        currentRoute == DayTodoRoute.Onboarding ||
        currentRoute in DayTodoRoute.AuthRoutes
    ) {
        Modifier.fillMaxSize()
    } else {
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DayTodoNavHost(
            navController = navController,
            onTodayScheduleChanged = { hasTodaySchedule = it },
            modifier = navHostModifier,
        )
        if (showBottomNavigation) {
            FloatingBottomNavigation(
                currentRoute = currentRoute,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 28.dp)
                    .padding(bottom = 56.dp),
                onDestinationClick = { destination ->
                    navController.navigateToTopLevelDestination(destination.route)
                },
            )
        }
    }
}
