package com.boostcamp.dailyfilm.presentation.login.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.boostcamp.dailyfilm.presentation.login.LoginRoute
import com.boostcamp.dailyfilm.presentation.navigation.MainDestination
import kotlinx.coroutines.CoroutineScope

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(MainDestination.LOGIN.route, navOptions)
}

fun NavGraphBuilder.loginScreen(
    onLogin: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    coroutineScope: CoroutineScope,
) {
    composable(
        route = MainDestination.LOGIN.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        LoginRoute(
            onLogin = onLogin,
            onShowSnackbar = onShowSnackbar,
            coroutineScope = coroutineScope
        )
    }
}