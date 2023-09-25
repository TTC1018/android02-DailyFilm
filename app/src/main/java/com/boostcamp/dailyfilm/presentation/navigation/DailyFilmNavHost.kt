package com.boostcamp.dailyfilm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.boostcamp.dailyfilm.presentation.DailyFilmAppState

@Composable
fun DailyFilmNavHost(
    modifier: Modifier = Modifier,
    appState: DailyFilmAppState,
    startDestination: String = MainDestination.LOGIN.route
) {
    val navController = appState.mainNavController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

    }
}