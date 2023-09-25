package com.boostcamp.dailyfilm.presentation.calendar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.boostcamp.dailyfilm.presentation.navigation.MainDestination

fun NavHostController.navigateToCalendar(navOptions: NavOptions? = null) {
    navigate(MainDestination.CALENDAR.route, navOptions)
}

fun NavGraphBuilder.calendarScreen() {
    composable(MainDestination.CALENDAR.route) {

    }
}