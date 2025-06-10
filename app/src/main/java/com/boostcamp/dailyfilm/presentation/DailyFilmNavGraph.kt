package com.boostcamp.dailyfilm.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boostcamp.dailyfilm.presentation.calendar.CalendarRoute
import com.boostcamp.dailyfilm.presentation.login.LoginRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun DailyFilmNavGraph(
    navController: NavHostController,
    navActions: DailyFilmNavigationActions,
    onShowSnackBar: suspend (String) -> Unit,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    startDestination: String = DailyFilmDestinations.LOGIN_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = DailyFilmDestination.DailyFilmRoute
    ) {
        navigation<DailyFilmDestination.DailyFilmRoute>(
            startDestination = DailyFilmDestination.Login,
        ) {
            composable<DailyFilmDestination.Login>(
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                LoginRoute(
                    onShowSnackBar = onShowSnackBar,
                    navigateToCalendar = navActions.navigateToCalendar,
                    coroutineScope = coroutineScope,
                )
            }

            composable<DailyFilmDestination.Calendar>(
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                CalendarRoute(
                    coroutineScope = coroutineScope,
                )
            }
        }
    }
}
