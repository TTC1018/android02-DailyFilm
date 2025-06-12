package com.boostcamp.dailyfilm.presentation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import com.boostcamp.dailyfilm.presentation.DailyFilmDestination.*
import com.boostcamp.dailyfilm.presentation.calendar.model.DateModel
import com.boostcamp.dailyfilm.presentation.playfilm.model.EditState
import com.boostcamp.dailyfilm.presentation.selectvideo.DateNavigationModel

object DailyFilmDestinations {
    const val LOGIN_ROUTE = "login"
    const val CALENDAR_ROUTE = "calendar"
}

internal sealed interface DailyFilmDestination {
    @Serializable
    data object DailyFilmRoute : DailyFilmDestination

    @Serializable
    data object Login : DailyFilmDestination

    @Serializable
    data object Calendar : DailyFilmDestination

    @Serializable
    data class SelectVideo(
        val dateModel: DateNavigationModel,
        val calendarIndex: Int,
        val editState: EditState,
    ) : DailyFilmDestination
}

class DailyFilmNavigationActions(navController: NavHostController) {
    val navigateToLogin: () -> Unit = {
        navController.navigate(Login) {
            popUpTo(Login) { inclusive = true }
            launchSingleTop = true
        }
    }

    val navigateToCalendar: () -> Unit = {
        navController.navigate(Calendar) {
            popUpTo(Calendar) { inclusive = true }
            launchSingleTop = true
        }
    }
}
