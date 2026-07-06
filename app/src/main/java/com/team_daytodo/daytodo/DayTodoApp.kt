package com.team_daytodo.daytodo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team_daytodo.daytodo.feature.home.HomeRoute

private const val HOME_ROUTE = "home"

@Composable
fun DayTodoApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
    ) {
        composable(HOME_ROUTE) {
            HomeRoute()
        }
    }
}
