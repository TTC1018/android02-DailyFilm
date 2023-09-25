package com.boostcamp.dailyfilm.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.boostcamp.dailyfilm.presentation.navigation.MainDestination
import com.boostcamp.dailyfilm.presentation.navigation.MainDestination.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberDailyFilmAppState(
    mainNavController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): DailyFilmAppState = remember {
    DailyFilmAppState(
        mainNavController,
        coroutineScope
    )
}

@Stable
class DailyFilmAppState(
    val mainNavController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    val currentDestination: NavDestination?
        @Composable get() = mainNavController
            .currentBackStackEntryAsState().value?.destination

    val currentMainDestination: MainDestination
        @Composable get() = when (currentDestination?.route) {
            LOGIN.route -> LOGIN
            CALENDAR.route -> CALENDAR
            SEARCH_FILM.route -> SEARCH_FILM
            PLAY_FILM.route -> PLAY_FILM
            SEARCH_FILM.route -> SEARCH_FILM
            SELECT_VIDEO.route -> SELECT_VIDEO
            TRIM_VIDEO.route -> TRIM_VIDEO
            UPLOAD_FILM.route -> UPLOAD_FILM
            TOTAL_FILM.route -> TOTAL_FILM
            SETTINGS.route -> SETTINGS
            else -> throw Exception("정의되지 않은 Destination")
        }
}