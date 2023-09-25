package com.boostcamp.dailyfilm.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.boostcamp.dailyfilm.presentation.navigation.DailyFilmNavHost

@Composable
fun DailyFilmApp(
    modifier: Modifier = Modifier,
    appState: DailyFilmAppState = rememberDailyFilmAppState()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    DailyFilmNavHost(
        modifier = modifier.fillMaxSize(),
        appState = appState,
        onShowSnackbar = { message ->
            snackbarHostState.showSnackbar(message)
        }
    )
}