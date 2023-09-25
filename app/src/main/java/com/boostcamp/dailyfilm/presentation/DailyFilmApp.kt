package com.boostcamp.dailyfilm.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boostcamp.dailyfilm.presentation.navigation.DailyFilmNavHost

@Composable
fun DailyFilmApp(
    modifier: Modifier = Modifier,
    appState: DailyFilmAppState = rememberDailyFilmAppState()
) {
    DailyFilmNavHost(
        modifier = modifier,
        appState = appState,
    )
}