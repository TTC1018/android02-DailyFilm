package com.boostcamp.dailyfilm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.boostcamp.dailyfilm.presentation.DailyFilmAppState
import com.boostcamp.dailyfilm.presentation.calendar.navigation.calendarScreen
import com.boostcamp.dailyfilm.presentation.calendar.navigation.navigateToCalendar
import com.boostcamp.dailyfilm.presentation.login.navigation.loginScreen

@Composable
fun DailyFilmNavHost(
    modifier: Modifier = Modifier,
    appState: DailyFilmAppState,
    onShowSnackbar: suspend (String) -> Unit,
    startDestination: String = MainDestination.LOGIN.route,
) {
    val navController = appState.mainNavController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        loginScreen(
            onLogin = navController::navigateToCalendar,
            onShowSnackbar = onShowSnackbar,
            coroutineScope = appState.coroutineScope
        )
        calendarScreen()
    }
}