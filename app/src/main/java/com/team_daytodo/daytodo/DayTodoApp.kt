package com.team_daytodo.daytodo

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun DayTodoApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var hasTodaySchedule by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomNavigation = currentRoute != DayTodoRoute.CourseJoin

    Box(modifier = Modifier.fillMaxSize()) {
        DayTodoNavHost(
            navController = navController,
            onTodayScheduleChanged = { hasTodaySchedule = it },
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
                    if (destination.route == DayTodoRoute.Today && !hasTodaySchedule) {
                        Toast.makeText(context, "오늘은 일정이 없어요.", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigateToTopLevelDestination(destination.route)
                    }
                },
            )
        }
    }
}
